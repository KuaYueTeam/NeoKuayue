package willow.train.kuayue;

import kasuga.lib.core.config.SimpleConfig;

public class KuayueConfig {

    public static final SimpleConfig CONFIG = new SimpleConfig()
            .client("Kuayue Client")
            .boolConfig("RECEIVE_COLOR_SHARE",
                    "Should receive color requests.", true)
            .doubleConfig("OVERHEAD_LINE_END_WEIGHT_HEIGHT",
                    "The height of the end weight", 3.0)
            .rangedDoubleConfig("OVERHEAD_LINE_SAGGING_COEFFICIENT",
                    "The sagging coefficient of the overhead line", 300.0, 1.0, 100000.0)
            .rangedIntConfig("OVERHEAD_LINE_SUPPORT_RENDER_DISTANCE",
                    "This value controls how far you could see those overhead line supports.",
                    128, 32, 65535)
            .rangedIntConfig("PANTOGRAPH_FRESH_INTERVAL_TICKS_CLIENT",
                    "How often (in ticks) the pantograph matches the overhead line from client side",
                    1, 1, 20)

            .server("Kuayue Server")
            .rangedIntConfig("PANTOGRAPH_FRESH_INTERVAL_TICKS_SERVER",
                    "How often (in ticks) the pantograph matches the overhead line from server side",
                    10, 1, 20)
            .rangedIntConfig("PANTOGRAPH_SYNC_TICKS",
                    "the server would send pantograph sync packets to all clients in every <this> ticks.",
                    100, 1, 500)
            .rangedIntConfig("TECH_TREE_TRANSMISSION_TIMEOUT",
                    "the timeout of the transmission of tech tree packets from server to client (milliseconds)",
                    500, 100, 10000)
            .rangedIntConfig("TECH_TREE_TRANSMISSION_RETRY_TIMES",
                    "the retry times of the transmission of tech tree packets from server to client (times)",
                    10, 0, 100)
            .boolConfig("BOGEY_WEIGHT_SYS_ENABLE",
                    "Would the bogey weight system work.", false)
            .registerConfigs();

    public static void invoke(){}
}
