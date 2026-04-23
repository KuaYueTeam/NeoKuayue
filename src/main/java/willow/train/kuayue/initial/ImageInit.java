package willow.train.kuayue.initial;

import com.mojang.blaze3d.platform.NativeImage;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.bogey.BogeyStyle;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.utils.ResourceUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImageInit {
    private static boolean hasFired = false;

    private static ArrayList<ImageRegex> needToFire = new ArrayList<>();
    public static final HashMap<String, ImageRegex> images = new HashMap<>();

    private static void putImageIn(BogeyStyle style) throws URISyntaxException, IOException {

        Map<String, InputStream> streamMap = ResourceUtil.getFileStream("textures/gui/bogey_style/" + style.id.toDebugFileName(), false);

        for(BogeySizes.BogeySize size : style.validSizes()) {

            // 跳过所有反向的，因为反向的和正向的贴图一样
            if(size.id().toDebugFileName().endsWith("_backward"))
                continue;

            if(!streamMap.containsKey(size.id().toDebugFileName() + ".png"))
                continue;

            ResourceLocation location = new ResourceLocation(Kuayue.MODID, "textures/gui/bogey_style/" +
                    style.id.toDebugFileName() + "/" + size.id().toDebugFileName() + ".png");

            images.put(
                    style.id.toDebugFileName() + "." + size.id().toDebugFileName(),
                    new ImageRegex(location, streamMap.get(size.id().toDebugFileName() + ".png"), true)
            );

            streamMap.get(size.id().toDebugFileName() + ".png").close();
        }
    }

    private static ImageRegex register(ResourceLocation location, @Nullable Integer index) {
        try {
            Map<String, InputStream> streamMap = ResourceUtil.getFileStream(location.getPath(), true);
            if(streamMap.isEmpty()) return null;
            int counter = 0;
            for(String name : streamMap.keySet()) {
                if((index == null || (index != null && index.equals(0))) && counter == 0) {
                    return new ImageRegex(location, streamMap.get(name), true);
                } else if (index != null && index.equals(counter)) {
                    return new ImageRegex(location, streamMap.get(name), true);
                }
                counter++;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static ImageRegex register(String path, @Nullable Integer index) {
        if(!hasFired) {
            ImageRegex regex = new ImageRegex(new ResourceLocation(Kuayue.MODID, path));
            needToFire.add(regex);
            return regex;
        }
        ImageRegex regex = register(new ResourceLocation(Kuayue.MODID, path), index);
        if(regex == null) {
            CrashReport.forThrowable(new NoSuchFileException(path), "Maybe we'll find it next time.");
        }
        return regex;
    }

    public static class ImageRegex {

        public int width, height;

        public final ResourceLocation location;
        public BufferedImage image;
        public byte[] bytes;


        public ImageRegex(ResourceLocation location, BufferedImage image, int width, int height, byte[] bytes) {
            this.width = width;
            this.height = height;
            this.location = location;
            this.image = image;
            this.bytes = bytes;
        }

        public ImageRegex(ResourceLocation location) {
            this.location = location;
        }

        public void setImage(BufferedImage image) throws IOException {
            this.image = image;
            this.width = image.getWidth();
            this.height = image.getHeight();
        }

        public ImageRegex(FriendlyByteBuf buf) throws IOException {
            this.location = buf.readResourceLocation();
            int byteLength = buf.readInt();
            this.bytes = buf.readByteArray(byteLength);
            this.image = ImageIO.read(new ByteArrayInputStream(bytes));
            this.width = image.getWidth();
            this.height = image.getHeight();
        }

        public ImageRegex(ResourceLocation location, InputStream imageStream, boolean fromJar) throws IOException {
            this.location = location;
            setImage(ImageIO.read(imageStream));
            this.bytes = imageStream.readAllBytes();
            if(!fromJar) {
                NativeImage ni = NativeImage.read(imageStream);
                Minecraft.getInstance().textureManager.
                        register(new ResourceLocation(location.getNamespace(), location.getPath()),
                                new DynamicTexture(ni));
            }
        }

        public int getColor(int x, int y) {
            return image.getRGB(x, y);
        }

        public void toByteBuf(FriendlyByteBuf buf) {
            buf.writeResourceLocation(location);
            buf.writeInt(bytes.length);
            buf.writeByteArray(bytes);
        }

        public double aspectRatio() {
            return (double) width / (double) height;
        }
    }
}
