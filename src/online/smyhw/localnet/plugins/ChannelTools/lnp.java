package online.smyhw.localnet.plugins.ChannelTools;

import online.smyhw.localnet.message;
import online.smyhw.localnet.command.cmdManager;
import online.smyhw.localnet.data.DataManager;
import online.smyhw.localnet.data.config;
import online.smyhw.localnet.event.ChatINFO_Event;
import online.smyhw.localnet.event.EventManager;
import online.smyhw.localnet.lib.CommandFJ;
import online.smyhw.localnet.network.Client_sl;
import online.smyhw.localnet.plugins.PluginsManager;
import online.smyhw.localnet.plugins.channel.*;;

public class lnp 
{
	public static config CLconfig = new config();
	public static void plugin_loaded()
	{
		message.info("频道工具插件加载");
		try 
		{
			if(!PluginsManager.find("online.smyhw.localnet.plugins.channel"))
			{message.warning("频道工具依赖频道插件!但是没有检测到频道插件!");}
			cmdManager.add_cmd("ctools", lnp.class.getMethod("cmd", new Class[]{Client_sl.class,String.class}));
			EventManager.AddListener("ChatINFO", lnp.class.getMethod("lr", new Class[] {ChatINFO_Event.class}));
		} 
		catch (Exception e) 
		{
			message.warning("频道工具插件加载错误!");
			e.printStackTrace();
		}
		CLconfig = DataManager.LoadConfig("./configs/ChannelTools.config");
	}
	
	public static void cmd(Client_sl User,String cmd)
	{
		message.info("得到指令:"+cmd);
		String temp1 = CommandFJ.fj(cmd, 1);
		switch(temp1)
		{
		case "help":
		{
			User.sendMsg("Channel Tools\n"
					+ "nospeak <频道ID> --将某个频道禁言\n"
					+ "---\n"
					+ "powered by smyhw");
			return;
		}
		case "nospeak":
		{
			if(CommandFJ.js(cmd)<3) 
			{
				User.sendMsg("参数不足");
				return;
			}
			String temp2 = CommandFJ.fj(cmd, 2);
			CLconfig.set("nospeak."+temp2, true);
			DataManager.SaveConfig("./configs/ChannelTools.config", CLconfig);
			User.sendMsg("已将频道<"+temp2+">禁言");
			return;
		}
		default:
		{
			User.sendMsg("未知的ChannelTools指令");
			return;
		}
		}
	}
	
	public static void lr(ChatINFO_Event dd)
	{
		int pd = lib.getChannel(dd.From_User.remoteID);
		if(CLconfig.get_boolean("nospeak."+pd)) {dd.Cancel=true;}
	}
	

}
