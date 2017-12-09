package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class TakumiConfigCore {
    
    public static final String GENERAL = "General";
    private static final String HOGE = GENERAL + ".hoge";
    public static Configuration cfg;
    public static boolean isTransparentCeruleanCreeper = true;
    public static byte amountSmelting = 1;
    
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
        cfg.addCustomCategoryComment(GENERAL, "A settings of Takumicraft.");
        cfg.setCategoryLanguageKey(GENERAL, "config.takumicraft.category.general");
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
        isTransparentCeruleanCreeper = cfg.getBoolean("isTransparentCeruleanCreeper", GENERAL, isTransparentCeruleanCreeper,
                                                      TakumiUtils.takumiTranslate("config.takumicraft.cerulean.desc"),
                                                      "config.takumicraft.cerulean.prop");
        // Difficulty
        //amountSmelting = (byte) cfg.getInt("amountSmelting", HOGE, amountSmelting, 1, Byte.MAX_VALUE, "Smelting amount of Aluminium Ingot from
        // Aluminium Ore.", "config.aluminium.prop.amountSmelting");
        // 設定内容をコンフィグファイルに保存する。
        cfg.save();
    }
}