package willow.train.kuayue.initial.create;

import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.foundation.block.IBE;
import kasuga.lib.registrations.create.InteractionReg;
import kasuga.lib.registrations.create.MovementReg;
import willow.train.kuayue.behaviour.*;
import willow.train.kuayue.block.panels.block_entity.IContraptionMovementBlockEntity;
import willow.train.kuayue.block.panels.carport.DF11GChimneyBlock;
import willow.train.kuayue.block.panels.carport.DF5ChimneyBlock;
import willow.train.kuayue.block.panels.end_face.CustomRenderedEndfaceBlock;
import willow.train.kuayue.block.seat.YZSeatBlock;
import willow.train.kuayue.initial.AllBlocks;
import willow.train.kuayue.initial.AllElements;
import willow.train.kuayue.initial.AllTags;
import willow.train.kuayue.systems.train_extension.conductor.registry.ConductorCandidateRegistry;

public class AllBehaviours {

    public static final InteractionReg<CompanyClickBehaviour> COMPANY_CLICK_BEHAVIOUR =
            new InteractionReg<CompanyClickBehaviour>("company_click_behaviour")
                    .behaviour(new CompanyClickBehaviour())
                    .sortByBlocks(
                            AllBlocks.COMPANY_TRAIN_PANEL,
                            AllBlocks.COMPANY_TRAIN_DOOR,
                            AllBlocks.COMPANY_SLIDING_DOOR
                    )
                    .submit(AllElements.testRegistry);

    public static final MovementReg<AnimationMovementBehaviour> ANIMATION_MOVEMENT_BEHAVIOUR =
            new MovementReg<AnimationMovementBehaviour>("animation_movement_behaviour")
                    .behaviour(new AnimationMovementBehaviour())
                    .statePredicate(
                            state -> state.getBlock() instanceof IBE<?> ibe &&
                                    IContraptionMovementBlockEntity.class.isAssignableFrom(ibe.getBlockEntityClass()) &&
                                    !(state.getBlock() instanceof DF11GChimneyBlock)
                    )
                    .submit(AllElements.testRegistry);

    public static final MovementReg<ChimneyMovementBehaviour> CHIMNEY_MOVEMENT_BEHAVIOUR =
            new MovementReg<ChimneyMovementBehaviour>("chimney_movement_behaviour")
                    .behaviour(new ChimneyMovementBehaviour())
                    .statePredicate(
                            state -> state.getBlock() instanceof DF11GChimneyBlock &&
                                    !(state.getBlock() instanceof DF5ChimneyBlock)
                    )
                    .submit(AllElements.testRegistry);

    public static final MovementReg<DF5ChimneyMovementBehaviour> DF5_CHIMNEY_MOVEMENT_BEHAVIOUR =
            new MovementReg<DF5ChimneyMovementBehaviour>("df5_chimney_movement_behaviour")
                    .behaviour(new DF5ChimneyMovementBehaviour())
                    .statePredicate(
                            state -> state.getBlock() instanceof DF5ChimneyBlock
                    )
                    .submit(AllElements.testRegistry);

    public static final InteractionReg<SeatClickBehaviour> SEAT_CLICK_BEHAVIOUR =
            new InteractionReg<SeatClickBehaviour>("seat_click_behaviour")
                    .behaviour(new SeatClickBehaviour())
                    .statePredicate(state -> (state.getBlock() instanceof YZSeatBlock && state.is(AllTags.MULTI_SEAT_BLOCK.tag())))
                    .submit(AllElements.testRegistry);

    public static final MovementReg<BogeyOverweightBehavior> BOGEY_OVERWEIGHT_BEHAVIOUR =
            new MovementReg<BogeyOverweightBehavior>("bogey_overweight_behaviour")
                    .behaviour(new BogeyOverweightBehavior())
                    .statePredicate(blockState ->  blockState.getBlock() instanceof AbstractBogeyBlock<?>)
                    .submit(AllElements.testRegistry);

    public static final InteractionReg<CouplerInteractionBehaviour> COUPLER_INTERACTION_BEHAVIOUR =
            new InteractionReg<CouplerInteractionBehaviour>("coupler_interaction_behaviour")
                    .behaviour(new CouplerInteractionBehaviour())
                    .statePredicate(blockState -> {
                        if(blockState.getBlock() instanceof CustomRenderedEndfaceBlock) return false;
                        return ConductorCandidateRegistry.getProvider(blockState) != null;
                    })
                    .submit(AllElements.testRegistry);

    public static final InteractionReg<EndfaceMovementBehaviour> END_FACE_MOVEMENT_BEHAVIOUR =
            new InteractionReg<EndfaceMovementBehaviour>("endface_movement_behaviour")
                    .behaviour(new EndfaceMovementBehaviour())
                    .statePredicate(
                            state -> state.getBlock() instanceof CustomRenderedEndfaceBlock
                    )
                    .submit(AllElements.testRegistry);

    public static void invoke(){}
}
