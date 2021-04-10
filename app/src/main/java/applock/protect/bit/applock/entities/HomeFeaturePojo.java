package applock.protect.bit.applock.entities;



public class HomeFeaturePojo
{
    private String _featureName;
    private int    _appIconId;


    public HomeFeaturePojo(String _featureName,int _appIconId)
    {
        this._featureName = _featureName;
        this._appIconId = _appIconId;
    }


    public int get_appIconId() {
        return _appIconId;
    }

    public void set_appIconId(int _appIconId) {
        this._appIconId = _appIconId;
    }

    public String get_featureName() {
        return _featureName;
    }

    public void set_featureName(String _featureName) {
        this._featureName = _featureName;
    }


}
