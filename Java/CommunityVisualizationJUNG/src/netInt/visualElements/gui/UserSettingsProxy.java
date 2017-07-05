package netInt.visualElements.gui;

public class UserSettingsProxy {

	private UserSettings settings;
	
	public UserSettingsProxy()
	{
		settings = UserSettings.getInstance();
	}
	
	public void setColorBackground(int color) {
		
		settings.setColorBackground(color);
	}
}
