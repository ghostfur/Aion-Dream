/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.archdaeva;

import com.aionemu.gameserver.model.*;
import com.aionemu.gameserver.model.gameobjects.*;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.*;
import com.aionemu.gameserver.questEngine.handlers.*;
import com.aionemu.gameserver.questEngine.model.*;
import com.aionemu.gameserver.services.*;
import com.aionemu.gameserver.services.instance.*;
import com.aionemu.gameserver.services.teleport.*;
import com.aionemu.gameserver.services.mail.*;
import com.aionemu.gameserver.utils.*;
import com.aionemu.gameserver.world.*;
import com.aionemu.gameserver.world.zone.*;

/****/
/** Author Rinzler (Encom)
/** Source KOR: https://www.youtube.com/watch?v=8Qt-ZODwhoA
/****/

public class _20520Lost_Destiny extends QuestHandler
{
	public static final int questId = 20520;
	private final static int[] npcs = {204191, 806077, 806080};
	
	public _20520Lost_Destiny() {
		super(questId);
	}
	
    @Override
    public void register() {
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
		qe.registerOnLogOut(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerQuestItem(182215974, questId);
		qe.registerOnEnterZone(ZoneName.get("DF6_SENSORY_AREA_Q20520_220110000"), questId);
    }
	
	@Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
        if (player.getWorldId() == 120010000) { //Pandaemonium.
            QuestState qs = player.getQuestStateList().getQuestState(questId);
            if (qs == null) {
                env.setQuestId(questId);
                if (QuestService.startQuest(env)) {
					SystemMailService.getInstance().sendMail("Munin", player.getName(), "[ArchDaeva]",
					"Congratulations Daeva, I'am Peregrine and I expect you at Norsvold. Come join me.", 182215974, 1, 0, LetterType.NORMAL);
                    return true;
                }
            }
        }
        return false;
    }
	
    @Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806077) { //Edorin.
				switch (env.getDialog()) {
					case QUEST_SELECT: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					} case SELECT_ACTION_1353: {
						if (var == 1) {
							return sendQuestDialog(env, 1353);
						}
					} case SETPRO1: {
						changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 204191) { //Doman.
				switch (env.getDialog()) {
					case QUEST_SELECT: {
						if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
					} case SELECT_ACTION_1694: {
						if (var == 2) {
							return sendQuestDialog(env, 1694);
						}
					} case SETPRO2: {
						TeleportService2.teleportTo(env.getPlayer(), 220110000, 1757.3667f, 2008.911f, 196.59653f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
						removeQuestItem(env, 182215974, 1);
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 806080) { //Feregran.
				switch (env.getDialog()) {
					case QUEST_SELECT: {
						if (var == 4) {
							return sendQuestDialog(env, 2375);
						}
					} case SET_SUCCEED: {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806080) { //Feregran.
                if (env.getDialog() == DialogAction.QUEST_SELECT) {
                    return sendQuestDialog(env, 10002);
                } else {
                    int[] norsvoldMission = {20521, 20522, 20523, 20524, 20525, 20526, 20527, 20528, 20529};
                    for (int quest: norsvoldMission) {
                        QuestEngine.getInstance().onEnterZoneMissionEnd(new QuestEnv(env.getVisibleObject(), env.getPlayer(), quest, env.getDialogId()));
                    }
                    return sendQuestEndDialog(env);
                }
            }
        }
		return false;
	}
	
	@Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			int instanceId = player.getInstanceId();
			if (zoneName == ZoneName.get("DF6_SENSORY_AREA_Q20520_220110000")) {
				if (var == 3) {
					playQuestMovie(env, 867);
					changeQuestStep(env, 3, 4, false);
					ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							if (player != null) {
							    WorldMapInstance SanctuaryDungeon = InstanceService.getNextAvailableInstance(301580000);
							    InstanceService.registerPlayerWithInstance(SanctuaryDungeon, player);
							    TeleportService2.teleportTo(player, 301580000, SanctuaryDungeon.getInstanceId(), 431, 491, 99);
							}
						}
					}, 43000);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
    public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 0) {
                return HandlerResult.fromBoolean(useQuestItem(env, item, 0, 1, false));
            }
        }
        return HandlerResult.FAILED;
    }
	
	@Override
    public boolean onLogOutEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 4) {
                qs.setQuestVar(1);
                updateQuestStatus(env);
                return true;
            }
        }
        return false;
    }
}