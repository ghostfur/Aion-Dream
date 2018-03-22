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

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.*;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.*;
import com.aionemu.gameserver.questEngine.handlers.*;
import com.aionemu.gameserver.questEngine.model.*;
import com.aionemu.gameserver.services.*;
import com.aionemu.gameserver.world.*;
import com.aionemu.gameserver.world.zone.*;

/****/
/** Author Rinzler (Encom)
/****/

public class _20525Agent_Peregrine_Call extends QuestHandler
{
    public static final int questId = 20525;
	private final static int[] npcs = {806079, 806135, 806228, 806229, 806230, 806231};
	
    public _20525Agent_Peregrine_Call() {
        super(questId);
    }
	
    @Override
    public void register() {
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(182216084, questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnEnterZone(ZoneName.get("DF6_ITEMUSEAREA_Q20525"), questId);
    }
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 20521, true);
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
            if (targetId == 806079) { //Feregran.
                switch (env.getDialog()) {
                    case QUEST_SELECT: {
                        if (var == 0) {
                            return sendQuestDialog(env, 1011);
                        }
					} case SELECT_ACTION_1012: {
						if (var == 0) {
							return sendQuestDialog(env, 1012);
						}
					} case SETPRO1: {
                        changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 806135) { //Conrto.
				switch (env.getDialog()) {
					case QUEST_SELECT: {
						if (var == 1) {
							return sendQuestDialog(env, 1353);
						} else if (var == 3) {
							return sendQuestDialog(env, 2036);
						} else if (var == 4) {
							return sendQuestDialog(env, 2375);
						} else if (var == 5) {
							return sendQuestDialog(env, 2716);
						}
					} case SETPRO2: {
						changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					} case SETPRO4: {
						changeQuestStep(env, 3, 4, false);
						return closeDialogWindow(env);
					} case SETPRO6: {
						removeQuestItem(env, 182216081, 20); 
						removeQuestItem(env, 182216082, 20); 
						removeQuestItem(env, 182216083, 5);
						return defaultCloseDialog(env, 5, 6, false, false, 182216084, 1, 0, 0);
					} case CHECK_USER_HAS_QUEST_ITEM: {
						return checkQuestItems(env, 4, 5, false, 10000, 10001);
					} case FINISH_DIALOG: {
						if (var == 5) {
							defaultCloseDialog(env, 5, 5);
						} else if (var == 4) {
							defaultCloseDialog(env, 4, 4);
						}
					}
				}
			} if (targetId == 806228 || //Bastok.
			    targetId == 806229 || //Duisys.
				targetId == 806230 || //Sieden.
				targetId == 806231) { //Norte.
                switch (env.getDialog()) {
				    case QUEST_SELECT: {
                        changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
                    }
                }
            }
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806079) { //Feregran.
                if (env.getDialog() == DialogAction.QUEST_SELECT) {
                    return sendQuestDialog(env, 10002);
				} else if (env.getDialog() == DialogAction.SELECT_QUEST_REWARD) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
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
			if (!player.isInsideZone(ZoneName.get("DF6_ITEMUSEAREA_Q20525"))) {
				return HandlerResult.UNKNOWN;
			}
            int var = qs.getQuestVarById(0);
            if (var == 6) {
				giveQuestItem(env, 182216084, 1); 
				giveQuestItem(env, 182216085, 1);
                return HandlerResult.fromBoolean(useQuestItem(env, item, 6, 6, true));
            }
        }
        return HandlerResult.FAILED;
    }
}