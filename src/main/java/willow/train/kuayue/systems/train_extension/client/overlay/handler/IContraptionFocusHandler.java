package willow.train.kuayue.systems.train_extension.client.overlay.handler;

import willow.train.kuayue.systems.train_extension.client.overlay.TrainOverlayContext;

@FunctionalInterface
public interface IContraptionFocusHandler {
    /*
     * Handles the overlay of current targeting block in a contraption.
     */
    boolean handle(TrainOverlayContext context);
}
