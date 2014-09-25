package globals;
import de.looksgood.ani.Ani;

/**
 * The goal of this class is to have Ani available from any place of the application.
 * There is no need to pass Ani as a parameter in every object.
 *
 * This is a ONE INSTANCE class. As a singleton, this class is instanced just once.
 *
 * @author alejandro
 */
public class AniSingleton {
	// "static" IS THE KEY WORD HERE
    private static AniSingleton ANI_INSTANCE = new AniSingleton();

    private AniSingleton() {}

    public static AniSingleton getInstance() {
        return ANI_INSTANCE;
    }

    //--------
    
    private Ani aniClass;

    public void setAni(Ani _aniClass){
    	this.aniClass = _aniClass;
	}

    public Ani getAni() {
		return aniClass;
	}
}