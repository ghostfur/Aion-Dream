package com.aionemu.gameserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.EnchantsConfig;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class AuthorizeService {
	private static final Logger log = LoggerFactory.getLogger(AuthorizeService.class);
	
	public void onSetEquipAccessories(Player player, int tempering) {
		for(Item targetItem : player.getEquipment().getEquippedItemsWithoutStigma()) {
			if (!targetItem.getItemTemplate().isPlume() && !targetItem.getItemTemplate().isBracelet() && targetItem.getItemTemplate().isAccessory() && targetItem.getAuthorize() > EnchantsConfig.MAX_CAP_AUTHORIZED_ACCESORIES) {
				log.warn("Player : " + player.getName() + " have more than authorize level : " + targetItem.getAuthorize());
				
				if (isTempering(targetItem)) {
					if (targetItem.getAuthorize() == tempering) {
						continue;
					} if (tempering > EnchantsConfig.MAX_CAP_AUTHORIZED_ACCESORIES) {
						tempering = EnchantsConfig.MAX_CAP_AUTHORIZED_ACCESORIES;
					} if (tempering < 0) {
						tempering = 0;
					}
					targetItem.setAuthorize(tempering);
					if (targetItem.isEquipped()) {
						player.getGameStats().updateStatsVisually();
					}
					ItemPacketService.updateItemAfterInfoChange(player, targetItem);
					PacketSendUtility.sendMessage(player, "Accessories has been reduce to + " + EnchantsConfig.MAX_CAP_AUTHORIZED_ACCESORIES);
				}
			}
			
		}
	}
	
	public void onSetEquipPlume(Player player, int tempering) {
		for(Item targetItem : player.getEquipment().getEquippedItemsWithoutStigma()) {
			if (targetItem.getItemTemplate().isPlume() && targetItem.getAuthorize() > EnchantsConfig.MAX_CAP_AUTHORIZED_PLUME) {
				log.warn("Player : " + player.getName() + " have more than authorize level : " + targetItem.getAuthorize());
				
				if (isPlume(targetItem)) {
					if (targetItem.getAuthorize() == tempering) {
						continue;
					} if (tempering > EnchantsConfig.MAX_CAP_AUTHORIZED_PLUME) {
						tempering = EnchantsConfig.MAX_CAP_AUTHORIZED_PLUME;
					} if (tempering < 0) {
						tempering = 0;
					}
					targetItem.setAuthorize(tempering);
					if (targetItem.isEquipped()) {
						player.getGameStats().updateStatsVisually();
					}
					ItemPacketService.updateItemAfterInfoChange(player, targetItem);
					PacketSendUtility.sendMessage(player, "Plume has been reduce to + " + EnchantsConfig.MAX_CAP_AUTHORIZED_PLUME);
				}
			}
			
		}
	}
	
	private static boolean isTempering(Item item) {
		if (item.getItemTemplate().isArmor()) {
			int pt = item.getItemTemplate().getItemSlot();
			if (pt == 192 || /* Earring */
				pt == 4 || /* Helm */
				pt == 768 || /* Rings */
				pt == 1024 || /* Necklace */
				pt == 65536 || /* Belt */
				pt == 2097152) { /* Bracelet */
				return true;
			}
		}
		return false;
	}
	
	private static boolean isPlume(Item item) {
		if (item.getItemTemplate().isArmor()) {
			int pt = item.getItemTemplate().getItemSlot();
			if (pt == 524288) {
				return true;
			}
		}
		return false;
	}
	
	public static final AuthorizeService getInstance() {
		return SingletonHolder.instance;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {
		protected static final AuthorizeService instance = new AuthorizeService();
	}
}