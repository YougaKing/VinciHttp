package core;

/**
 * author: YougaKingWu@gmail.com
 * created on: 2018/03/30 16:05
 * description:
 */
public class TransactionState {


    private int appPhase;
    private NetworkPhase networkInPhase;

    public void setAppPhase(int appPhase) {
        this.appPhase = appPhase;
    }


    public void setNetworkInPhase(NetworkPhase networkInPhase) {
        this.networkInPhase = networkInPhase;
    }
}
