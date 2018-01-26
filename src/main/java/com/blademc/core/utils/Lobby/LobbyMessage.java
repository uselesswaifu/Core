package com.blademc.core.utils.Lobby;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.utils.TextFormat;

import java.util.Random;

public class LobbyMessage extends NukkitRunnable {

	public static String messages[] = { "Follow us on Twitter: @BladePlexus", "More Games are in the will Come soon!",
			"Check out Our Website: blademc.pw", "Hope you have Fun!",
			"Hacking is NOT allowed on the BladeMC Cult, Disable client mods before playing",
			"You will always be by my side, oh sorry I was singing",

			"We know our server are not perfect we will always try to improve :)",
			"Find Any Bugs? Message us on Twitter @BladePlexus", "It works better if you plug it in.",
			"I have not lost my mind, it's backed up on disk", "Thanks for playing on the BladeMC Cult! Join a game!",

			"/msg send a private message to the given player", "To return to the lobby at any time, use /lobby",
			"Do witches use Spell Check?", "With Great power, Comes Great Electricity Bil",
			"Everyone has good qualities",

			"I think my iPhone is broken, I pressed the home button and I'm still at work",
			"Sometimes we all have those lazy moments", "Lazy Rule: if I can't Reach it, Then I do not need it",
			"Follow your heart, But Take your brain with you", "Nothing is as easy as it looks.",

			"Everything takes longer than you think.", "Whenever possible blame the Hardware",
			"Pirate software comes with a treasure map", "Admins can be annoying sometimes, but we all love them :)",
			"1000 Followers on Mobcrush can get you a Cool Mobcrush Rank",

			"Do not ask to be admin", "Need Help? Try typing /help", "Invite your friends to play :)",
			"Thank you for helping us Grow", "Speak kindly to one another",

			"Proofread carefully to see if you any words out!", "Some days you're the windshield, some days the bug",
			"We're lost but we're making good time", "What if there were no hypothetical questions?",
			"What was the best thing Before sliced bread?",

			"You used to be indecisive. Now you're not sure", "You will become rich and famous unless you don't",
			"You will never be younger then you are today..",
			"Sometimes you might not be the best at PvP, But remember there is always someone worse at PvP than you",
			"Never leave your Guard down" };

	@Override
	public void run() {
		final Random random = new Random();
		final int index = random.nextInt(messages.length);
		for (final Player player : Server.getInstance().getDefaultLevel().getPlayers().values())
			player.sendMessage(TextFormat.BOLD + "" + TextFormat.YELLOW + "- " + TextFormat.RESET + ""
					+ TextFormat.GRAY + messages[index]);

	}
}
