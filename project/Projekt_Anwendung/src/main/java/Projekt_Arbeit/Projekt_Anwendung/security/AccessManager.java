package Projekt_Arbeit.Projekt_Anwendung.security;

import Projekt_Arbeit.Projekt_Anwendung.entities.Broker;
import Projekt_Arbeit.Projekt_Anwendung.entities.Investor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccessManager {

    private Map<AccessToken, Investor> investorAccessList = new ConcurrentHashMap<>();

    private Map<AccessToken, Broker> brokerAccessList = new ConcurrentHashMap<>();

    public AccessToken createInvestorToken(Investor investor){

        String uuid = UUID.randomUUID().toString();
        AccessToken accessToken = new AccessToken(uuid);

        investorAccessList.put(accessToken, investor);

        return accessToken;
    }
    public AccessToken createBrokerToken(Broker broker)
    {

        String uuid = UUID.randomUUID().toString();
        AccessToken accessToken = new AccessToken(uuid);

        brokerAccessList.put(accessToken, broker);

        return accessToken;
    }

    public boolean removeInvestorTokenAfterDelete(AccessToken accessToken)
    {
        return investorAccessList.remove(accessToken) !=null;
    }

    public boolean removeBrokerTokenAfterDelete(AccessToken accessToken)
    {
        return brokerAccessList.remove(accessToken) !=null;
    }

    public boolean brokerhasAccess(AccessToken accessToken)
    {
        return brokerAccessList.containsKey(accessToken);
    }
    public Investor getInvestor(AccessToken accessToken)
    {
        return investorAccessList.get(accessToken);
    }

    public boolean investorhasToken(AccessToken accessToken)
    {
        return investorAccessList.containsKey(accessToken);
    }

    public boolean brokerhasToken(AccessToken accessToken)
    {
        return brokerAccessList.containsKey(accessToken);
    }
    public Broker getBroker(AccessToken accessToken)
    {
        return brokerAccessList.get(accessToken);
    }

    public boolean removeInvestorTokenAfterDeleteToken(AccessToken accessToken)
    {
        return investorAccessList.remove(accessToken) != null;
    }
}
