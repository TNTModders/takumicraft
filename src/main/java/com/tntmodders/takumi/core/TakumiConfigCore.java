package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class TakumiConfigCore {

    public static final boolean inEventServerClient = false;

    public static final String GENERAL = "General";
    private static final String SPAWN = GENERAL + ".Spawn";
    private static final String HOGE = GENERAL + ".hoge";
    public static Configuration cfg;
    public static boolean isTransparentCeruleanCreeper;
    public static int spawnWeightAnimal;
    public static byte amountSmelting = 1;
    public static boolean useTP;
    public static boolean TakumiHard;
    public static int rangeTakumiBookSync;
    public static boolean useNewYearTex;
    public static boolean ownerLockTakumiBlock;
    //debugConfig
    public static boolean inDev = false;
    /**
     * @TODO delete related code on release.
     */
    public static boolean inEventServer;

    public static void loadConfig(FMLPreInitializationEvent event) {
        // net.minecraftforge.common.config.Configurationのインスタンスを生成する。
        cfg = new Configuration(event.getSuggestedConfigurationFile(), TakumiCraftCore.VERSION, true);
        // 初期化する。
        initConfig();
        // コンフィグファイルの内容を変数と同期させる。
        syncConfig();
    }

    /**
     * コンフィグを初期化する。
     */
    private static void initConfig() {
        // カテゴリのコメントなどを設定する。
        // General
        cfg.addCustomCategoryComment(GENERAL, "Settings of TakumiCraft.");
        cfg.setCategoryLanguageKey(GENERAL, "config.takumicraft.category.general");
        //Spawn
        cfg.setCategoryComment(SPAWN, "Settings for cinfigure spawns");
        cfg.setCategoryLanguageKey(SPAWN, "config.takumicraft.category.spawn");
        // Difficulty
        //cfg.addCustomCategoryComment(HOGE, "The settings of hoge.");
        //cfg.setCategoryLanguageKey(HOGE, "config.takumicraft.category.hoge");
        //cfg.setCategoryRequiresMcRestart(HOGE, true);
    }

    /**
     * コンフィグを同期する。
     */
    public static void syncConfig() {
        // 各項目の設定値を反映させる。
        // General
        isTransparentCeruleanCreeper = cfg.getBoolean("isTransparentCeruleanCreeper", GENERAL, true,
                TakumiUtils.takumiTranslate("config" + ".takumicraft.cerulean.desc"),
                "config.takumicraft.cerulean.prop");

        //Spawn
        spawnWeightAnimal = cfg.getInt("spawnWeightAnimal", SPAWN, 15, 1, 50,
                TakumiUtils.takumiTranslate("config.takumicraft.weightanimal" + ".desc"),
                "config.takumicraft.weightanimal.prop");
        //TP
        useTP = cfg.getBoolean("isUseTP", GENERAL, false,
                TakumiUtils.takumiTranslate("config.takumicraft.useTP" + ".desc"), "config.takumicraft.useTP.prop");
        //TakumiHard
        TakumiHard = cfg.getBoolean("isTakumiHard", GENERAL, false,
                TakumiUtils.takumiTranslate("config.takumicraft.takumiHard.desc"),
                "config.takumicraft.takumiHard.prop");
        // Difficulty
        //amountSmelting = (byte) cfg.getInt("amountSmelting", HOGE, amountSmelting, 1, Byte.MAX_VALUE, "Smelting amount of Aluminium Ingot from
        // Aluminium Ore.", "config.aluminium.prop.amountSmelting");
        //TakumiBookSync
        rangeTakumiBookSync = cfg.getInt("useTakumiBookSync", GENERAL, 16, 0, Integer.MAX_VALUE,
                TakumiUtils.takumiTranslate("config.takumicraft.booksync.desc"), "config.takumicraft.booksync.prop");
        // 設定内容をコンフィグファイルに保存する。
        useNewYearTex = cfg.getBoolean("useNewYearTexture", GENERAL, true,
                TakumiUtils.takumiTranslate("config.takumicraft.nytex.desc"), "config.takumicraft.nytex.prop");

        inEventServer = cfg.getBoolean("ineventserver", GENERAL, false, "ONLY IN SERVER, FOR TCS EVENT", "null");

        ownerLockTakumiBlock = cfg.getBoolean("ownerLockTakumiBlock", GENERAL, true,
                TakumiUtils.takumiTranslate("config.takumicraft.oltb.desc"), "config.takumicraft.oltb.prop");

        cfg.save();
    }
}