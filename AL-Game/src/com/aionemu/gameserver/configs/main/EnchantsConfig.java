/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class EnchantsConfig {

	/**
	 * Supplement Additional Rates
	 */
	@Property(key = "gameserver.supplement.lesser", defaultValue = "1.0")
	public static float LESSER_SUP;
	@Property(key = "gameserver.supplement.regular", defaultValue = "1.0")
	public static float REGULAR_SUP;
	@Property(key = "gameserver.supplement.greater", defaultValue = "1.0")
	public static float GREATER_SUP;
	@Property(key = "gameserver.supplement.mythic", defaultValue = "1.0")
	public static float MYTHIC_SUP;
	/**
	 * ManaStone Rates
	 */
	@Property(key = "gameserver.base.manastone", defaultValue = "50")
	public static float MANA_STONE;
	@Property(key = "gameserver.base.enchant", defaultValue = "60")
	public static float ENCHANT_STONE;
	@Property(key = "gameserver.manastone.clean", defaultValue = "false")
	public static boolean CLEAN_STONE;
	@Property(key = "gameserver.manastone.enchant_cast_delay", defaultValue = "5000")
	public static int ENCHANT_CAST_DELAY;
	/**
	 * Godstone Rates
	 */
	@Property(key = "gameserver.godstone.base", defaultValue = "1000")
	public static int BASE_GODSTONE;
	/**
	 * Custom Rates
	 */
	@Property(key="gameserver.charged.stigma.max", defaultValue="5")
	public static int MAX_CHARGED_STIGMA;
	@Property(key="gameserver.max.cap.authorized.plume", defaultValue="20")
	public static int MAX_CAP_AUTHORIZED_PLUME;
	@Property(key="gameserver.max.cap.authorized.armor", defaultValue="20")
	public static int MAX_CAP_AUTHORIZED_ARMOR;
	@Property(key="gameserver.max.cap.authorized.weapon", defaultValue="20")
	public static int MAX_CAP_AUTHORIZED_WEAPON;
	@Property(key="gameserver.max.cap.authorized.accesories", defaultValue="20")
	public static int MAX_CAP_AUTHORIZED_ACCESORIES;
	@Property(key="gameserver.max.cap.authorized.wing", defaultValue="20")
	public static int MAX_CAP_AUTHORIZED_WING;
	@Property(key="gameserver.max.cap.enchant.armor1", defaultValue="20")
	public static int MAX_CAP_ENCHANT_ARMOR1;
	@Property(key="gameserver.max.cap.enchant.weapon1", defaultValue="20")
	public static int MAX_CAP_ENCHANT_WEAPON1;
	@Property(key="gameserver.max.cap.enchant.wing1", defaultValue="20")
	public static int MAX_CAP_ENCHANT_WING1;
	@Property(key="gameserver.max.cap.enchant.armor2", defaultValue="20")
	public static int MAX_CAP_ENCHANT_ARMOR2;
	@Property(key="gameserver.max.cap.enchant.weapon2", defaultValue="20")
	public static int MAX_CAP_ENCHANT_WEAPON2;
	@Property(key="gameserver.max.cap.enchant.wing2", defaultValue="20")
	public static int MAX_CAP_ENCHANT_WING2;
	@Property(key="gameserver.enable.enchant.alwayssuccess", defaultValue="true")
	public static boolean ENCHANT_ALWAYS_SUCCESS;
	@Property(key="gameserver.enable.temperance.alwayssuccess", defaultValue="true")
	public static boolean TEMPERANCE_ALWAYS_SUCCESS;
	@Property(key="gameserver.breakthrough.skill.min.level.type1", defaultValue="20")
	public static int BREAKTHROUGH_SKILL_MINLEVEL_TYPE1;
	@Property(key="gameserver.breakthrough.skill.min.level.type1_2", defaultValue="20")
	public static int BREAKTHROUGH_SKILL_MINLEVEL_TYPE1_2;
	@Property(key="gameserver.breakthrough.skill.min.level.type1_3", defaultValue="20")
	public static int BREAKTHROUGH_SKILL_MINLEVEL_TYPE1_3;
	@Property(key="gameserver.breakthrough.skill.min.level.type2", defaultValue="25")
	public static int BREAKTHROUGH_SKILL_MINLEVEL_TYPE2;
	@Property(key="gameserver.breakthrough.skill.min.level.type2_2", defaultValue="25")
	public static int BREAKTHROUGH_SKILL_MINLEVEL_TYPE2_2;
	@Property(key="gameserver.breakthrough.skill.min.level.type2_3", defaultValue="25")
	public static int BREAKTHROUGH_SKILL_MINLEVEL_TYPE2_3;
	@Property(key="gameserver.breakthrough.skill.reset.onmax.level", defaultValue="true")
	public static boolean BREAKTHROUGH_SKILL_RESET_ONMAX_LEVEL;
	@Property(key="gameserver.breakthrough.skill.fail.decresed.max", defaultValue="true")
	public static boolean BREAKTHROUGH_SKILL_FAIL_DECREASE_MAX;
	@Property(key="gameserver.skill.boost.max.level", defaultValue="10")
	public static int SKILL_BOOST_MAX_LEVEL;
	@Property(key="gameserver.destroy.item.when.fail.enchant", defaultValue="true")
	public static boolean DESTROY_ITEM_WHEN_FAIL_ENCHANT;
	@Property(key="gameserver.destroy.item.when.fail.temper", defaultValue="true")
	public static boolean DESTROY_ITEM_WHEN_FAIL_TEMPER;
	@Property(key="gameserver.destroy.essence.core.when.fail", defaultValue="true")
	public static boolean DESTROY_ESSENCE_CORE;
	@Property(key="gameserver.destroy.stigma.when.fail", defaultValue="true")
	public static boolean DESTROY_STIGMA_WHEN_FAIL;
	@Property(key="gameserver.destroy.archdaeva.item.onlevel", defaultValue="15")
	public static int DESTROY_ARCHDAEVA_ITEM_ONLEVEL;
	@Property(key="gameserver.destroy.archdaeva.item.percentage", defaultValue="50")
	public static int DESTROY_ARCHDAEVA_ITEM_PERCENTAGE;
}
