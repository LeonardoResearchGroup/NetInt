package netInt.visualElements.gui;

import java.util.Random;

public class UserSettingsProxy {

	private UserSettings settings;
	
	public UserSettingsProxy()
	{
		settings = UserSettings.getInstance();
	}
	
	public void setColorBackground() {
		Random r = new Random();
		int random = r.nextInt(128);
		settings.setColorBackground(random);
	}
}
