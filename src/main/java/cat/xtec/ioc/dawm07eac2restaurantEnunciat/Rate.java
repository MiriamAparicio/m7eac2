package cat.xtec.ioc.dawm07eac2restaurantEnunciat;

import java.util.List;
import javax.ejb.Stateful;

/**
 *
 * @author Alex
 */
@Stateful
public class Rate implements RateLocal {
    private List<RestClass> ratedRest;

    @Override
    public List<RestClass> getRatedRest() {
        return ratedRest;
    }

    @Override
    public void setRatedRest(List<RestClass> ratedRest) {
        this.ratedRest = ratedRest;
    }
}
