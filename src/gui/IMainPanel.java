package gui;

public interface IMainPanel {
	public void setResult(String result, float percentage);
	public void setMessage(String txt, int icon);
	public void setLoading();
	public void clear();
}
