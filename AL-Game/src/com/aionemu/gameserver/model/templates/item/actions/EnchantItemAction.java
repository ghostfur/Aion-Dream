package com.aionemu.gameserver.model.templates.item.actions;

import java.util.Iterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.EnchantsConfig;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.templates.item.EquipType;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemQuality;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.EnchantService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.world.World;

/**
 * @author Idhacker542
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="EnchantItemAction")
public class EnchantItemAction extends AbstractItemAction {
	private static final Logger log = LoggerFactory.getLogger(EnchantItemAction.class);
	@XmlAttribute(name="count")
	private int count;
	@XmlAttribute(name="min_level")
	private Integer min_level;
	@XmlAttribute(name="max_level")
	private Integer max_level;
	@XmlAttribute(name="manastone_only")
	private boolean manastone_only;
	@XmlAttribute(name="chance")
	private float chance;
  
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (isSupplementAction()) {
			return false;
		}
		if (targetItem == null) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
			return false;
		}
		if (!targetItem.getItemTemplate().isHighdaeva() && parentItem.getItemTemplate().isHighdaeva() && parentItem.getItemTemplate().getCategory() == ItemCategory.MANASTONE) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300469, new Object[] { targetItem.getName() }));
			AuditLogger.info(player, "Player trying socket archdaeva manastone to non archdaeva equipment.");
			return false;
		}
		if (targetItem.getItemTemplate().getAuthorize() != 0 && parentItem.getItemTemplate().getCategory() == ItemCategory.ENCHANTMENT) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300453, new Object[] { targetItem.getName() }));
			AuditLogger.info(player, "Player trying enchant non enchantable equipment");
			return false;
		}
		if (parentItem == null) {
			return false;
		}
		int msID = parentItem.getItemTemplate().getTemplateId() / 1000000;
		int tID = targetItem.getItemTemplate().getTemplateId() / 1000000;
		if ((msID == tID) && (tID == 140)) {
			return true;
		}
		if (tID == 187) {
			return true;
		}
		if (msID != 167 && msID != 166) {
			return false;
		}
		return true;
	}
  
	public void act(Player player, Item parentItem, Item targetItem) {
		act(player, parentItem, targetItem, null, 0, 1);
	}
  
	public void act(final Player player, final Item parentItem, final Item targetItem, final Item supplementItem, final int suppItemid, final int targetWeapon) {
		if (targetItem.getEnchantLevel() >= EnchantsConfig.MAX_CHARGED_STIGMA && parentItem.getItemTemplate().getCategory() == ItemCategory.STIGMA && targetItem.getItemTemplate().isStigma()) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300454, new Object[] { targetItem.getName() }));
			return;
		}
		ItemQuality targetQuality = targetItem.getItemTemplate().getItemQuality();
		
		switch (targetQuality) {
		case COMMON: 
		case RARE: 
		case LEGEND: 
			if (!EnchantsConfig.BREAKTHROUGH_SKILL_RESET_ONMAX_LEVEL && targetItem.getEnchantLevel() >= EnchantsConfig.MAX_CAP_ENCHANT_ARMOR1 && targetItem.getEquipmentType() == EquipType.ARMOR && !targetItem.getItemTemplate().isWing()) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300454, new Object[] { targetItem.getName() }));
				return;
			}
			if (!EnchantsConfig.BREAKTHROUGH_SKILL_RESET_ONMAX_LEVEL && targetItem.getEnchantLevel() >= EnchantsConfig.MAX_CAP_ENCHANT_WEAPON1 && targetItem.getEquipmentType() == EquipType.WEAPON) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300454, new Object[] { targetItem.getName() }));
				return;
			}
			if (!EnchantsConfig.BREAKTHROUGH_SKILL_RESET_ONMAX_LEVEL && targetItem.getEnchantLevel() >= EnchantsConfig.MAX_CAP_ENCHANT_WING1 && targetItem.getEquipmentType() == EquipType.ARMOR && targetItem.getItemTemplate().isWing()) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300454, new Object[] { targetItem.getName() })); return;
			}
			break;
		case UNIQUE: 
		case EPIC: 
		case MYTHIC: 
			if (!EnchantsConfig.BREAKTHROUGH_SKILL_RESET_ONMAX_LEVEL && targetItem.getEnchantLevel() >= EnchantsConfig.MAX_CAP_ENCHANT_ARMOR2 && targetItem.getEquipmentType() == EquipType.ARMOR && !targetItem.getItemTemplate().isWing()) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300454, new Object[] { targetItem.getName() }));
				return;
			}
			if (!EnchantsConfig.BREAKTHROUGH_SKILL_RESET_ONMAX_LEVEL && targetItem.getEnchantLevel() >= EnchantsConfig.MAX_CAP_ENCHANT_WEAPON2 && targetItem.getEquipmentType() == EquipType.WEAPON) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300454, new Object[] { targetItem.getName() }));
				return;
			}
			if (!EnchantsConfig.BREAKTHROUGH_SKILL_RESET_ONMAX_LEVEL && targetItem.getEnchantLevel() >= EnchantsConfig.MAX_CAP_ENCHANT_WING2 && targetItem.getEquipmentType() == EquipType.ARMOR && targetItem.getItemTemplate().isWing()) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300454, new Object[] { targetItem.getName() })); return;
			}
			break;
		default:
			break;
		}
		
		if (supplementItem != null && !checkSupplementLevel(player, supplementItem.getItemTemplate(), targetItem.getItemTemplate())) {
			return;
		}
    
		final int currentEnchant = targetItem.getEnchantLevel();
		final boolean isSuccess = isSuccess(player, parentItem, targetItem, supplementItem, targetWeapon);
    
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), parentItem.getObjectId().intValue(), parentItem.getItemTemplate().getTemplateId(), EnchantsConfig.ENCHANT_CAST_DELAY, 0, 0));
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {
			
			@Override
			public void run() {
				ItemTemplate itemTemplate = parentItem.getItemTemplate();
				if (itemTemplate.getCategory() == ItemCategory.ENCHANTMENT) {
					EnchantService.enchantItemAct(player, parentItem, targetItem, currentEnchant, isSuccess, supplementItem, suppItemid);
				} else if (itemTemplate.getCategory() == ItemCategory.STIGMA && parentItem.getItemTemplate().getCategory() == targetItem.getItemTemplate().getCategory()) {
					EnchantService.enchantStigmaAct(player, parentItem, targetItem, currentEnchant, isSuccess);
				} else {
					EnchantService.socketManastoneAct(player, parentItem, targetItem, targetWeapon, isSuccess);
				}
				
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), parentItem.getObjectId().intValue(), parentItem.getItemTemplate().getTemplateId(), 0, isSuccess ? 1 : 2, 384));
				
				if (CustomConfig.ENABLE_ENCHANT_ANNOUNCE) {
					if (itemTemplate.getCategory() == ItemCategory.ENCHANTMENT && targetItem.getEnchantLevel() == 15 && isSuccess) {
						Iterator<Player> iter = World.getInstance().getPlayersIterator();
						while (iter.hasNext()) {
							Player player2 = (Player)iter.next();
							if (player2.getRace() == player.getRace()) {
								PacketSendUtility.sendPacket(player2, SM_SYSTEM_MESSAGE.STR_MSG_ENCHANT_ITEM_SUCCEEDED_15(player.getName(), targetItem.getItemTemplate().getNameId()));
							}
						}
					} if (itemTemplate.getCategory() == ItemCategory.ENCHANTMENT && targetItem.getEnchantLevel() == 20 && isSuccess) {
						Iterator<Player> iter = World.getInstance().getPlayersIterator();
						while (iter.hasNext()) {
							Player player2 = (Player)iter.next();
							if (player2.getRace() == player.getRace()) {
								PacketSendUtility.sendPacket(player2, new SM_SYSTEM_MESSAGE(1402285, new Object[] { player.getName(), targetItem.getItemTemplate().getName() }));
							}
						}
					}
				}
			}
		}, EnchantsConfig.ENCHANT_CAST_DELAY));
	}
  
	private boolean isSuccess(Player player, Item parentItem, Item targetItem, Item supplementItem, int targetWeapon) {
		ItemQuality targetQuality = targetItem.getItemTemplate().getItemQuality();
		switch (targetQuality) {
		case COMMON: 
		case RARE: 
		case LEGEND: 
			if (targetItem.getEnchantLevel() >= EnchantsConfig.MAX_CAP_ENCHANT_ARMOR1 && parentItem.getItemTemplate().getCategory() == ItemCategory.ENCHANTMENT && targetItem.getItemTemplate().isArmor() && !targetItem.getItemTemplate().isPlume() && targetItem.getItemTemplate().getArmorType() != ArmorType.WING && !targetItem.getItemTemplate().isAccessory()) { 
				if (EnchantsConfig.BREAKTHROUGH_SKILL_RESET_ONMAX_LEVEL) {
					return false;
				}
			}
			if (targetItem.getEnchantLevel() >= EnchantsConfig.MAX_CAP_ENCHANT_WEAPON1 && parentItem.getItemTemplate().getCategory() == ItemCategory.ENCHANTMENT && targetItem.getItemTemplate().isWeapon()) {
				if (EnchantsConfig.BREAKTHROUGH_SKILL_RESET_ONMAX_LEVEL) {
					return false;
				}
			}
			if (targetItem.getEnchantLevel() >= EnchantsConfig.MAX_CAP_ENCHANT_WING1 && parentItem.getItemTemplate().getCategory() == ItemCategory.ENCHANTMENT && targetItem.getItemTemplate().isWing()) {
				if (EnchantsConfig.BREAKTHROUGH_SKILL_RESET_ONMAX_LEVEL) {
					return false;
				}
			}
			break;
		case UNIQUE: 
		case EPIC: 
		case MYTHIC: 
			if (targetItem.getEnchantLevel() >= EnchantsConfig.MAX_CAP_ENCHANT_ARMOR2 && parentItem.getItemTemplate().getCategory() == ItemCategory.ENCHANTMENT && targetItem.getItemTemplate().isArmor() && !targetItem.getItemTemplate().isPlume() && !targetItem.getItemTemplate().isWing() && !targetItem.getItemTemplate().isAccessory()) {
				if (EnchantsConfig.BREAKTHROUGH_SKILL_RESET_ONMAX_LEVEL) {
					return false;
				}
			}
			if (targetItem.getEnchantLevel() >= EnchantsConfig.MAX_CAP_ENCHANT_WEAPON2 && parentItem.getItemTemplate().getCategory() == ItemCategory.ENCHANTMENT && targetItem.getItemTemplate().isWeapon()) { 
				if (EnchantsConfig.BREAKTHROUGH_SKILL_RESET_ONMAX_LEVEL) {
					return false;
				}
			}	
			if (targetItem.getEnchantLevel() >= EnchantsConfig.MAX_CAP_ENCHANT_WING2 && parentItem.getItemTemplate().getCategory() == ItemCategory.ENCHANTMENT && targetItem.getItemTemplate().isWing()) { 
				if (EnchantsConfig.BREAKTHROUGH_SKILL_RESET_ONMAX_LEVEL) {
					return false;
				}
			}
			break;
		default:
			break;
		}
		
		if (EnchantsConfig.ENCHANT_ALWAYS_SUCCESS) {
			return true;
		}
		
		if (parentItem.getItemCreator().equalsIgnoreCase("Black Cloud Market")) {
			return true;
		}
		
		if (parentItem.getItemTemplate() != null) {
			ItemTemplate itemTemplate = parentItem.getItemTemplate();
			if (itemTemplate.getCategory() == ItemCategory.ENCHANTMENT || parentItem.getItemTemplate().getCategory() == targetItem.getItemTemplate().getCategory() && itemTemplate.getCategory() == ItemCategory.STIGMA) {
				return EnchantService.enchantItem(player, parentItem, targetItem, supplementItem);
			}
			return EnchantService.socketManastone(player, parentItem, targetItem, supplementItem, targetWeapon);
		}
		return false;
	}
  
	public int getCount() {
		return count;
	}
  
	public int getMaxLevel() {
		return max_level != null ? max_level.intValue() : 0;
	}
  
	public int getMinLevel() {
		return min_level != null ? min_level.intValue() : 0;
	}
  
	public boolean isManastoneOnly() {
		return manastone_only;
	}
  
	public float getChance() {
		return chance;
	}
  
	boolean isSupplementAction() {
		return (getMinLevel() > 0) || (getMaxLevel() > 0) || (getChance() > 0.0F) || (isManastoneOnly());
	}
  
	private boolean checkSupplementLevel(Player player, ItemTemplate supplementTemplate, ItemTemplate targetItemTemplate) {
		try {
			if (supplementTemplate.getCategory() != ItemCategory.ENCHANTMENT) {
				int minEnchantLevel = targetItemTemplate.getLevel();
				int maxEnchantLevel = targetItemTemplate.getLevel();
        
				EnchantItemAction action = supplementTemplate.getActions().getEnchantAction();
				if (action != null) {
					if (action.getMinLevel() != 0) {
						minEnchantLevel = action.getMinLevel();
					}
					if (action.getMaxLevel() != 0) {
						maxEnchantLevel = action.getMaxLevel();
					}
				}
				
				if (minEnchantLevel <= targetItemTemplate.getLevel() && maxEnchantLevel >= targetItemTemplate.getLevel()) {
					return true;
				}
				
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_ENCHANT_ASSISTANT_NO_RIGHT_ITEM);
				return false;
			}
		} catch (Exception ex) {
			log.error("Exception during checkSupplementLevel: Suplement  " + supplementTemplate.getTemplateId() + " Target Item : " + targetItemTemplate.getTemplateId());
			return false;
		}
		return true;
	}
}
