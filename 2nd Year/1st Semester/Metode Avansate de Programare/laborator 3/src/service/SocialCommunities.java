package service;

import Utils.Graf;
import domain.User;

import java.util.ArrayList;
import java.util.List;

public class SocialCommunities {

    private final SocialNetwork socialNetwork;
    private final Graf graf;

    public SocialCommunities(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
        this.graf = socialNetwork.getGraf();
    }

    public int connectedCommunities() {
        Long[] result = graf.countComponentsAndGetLongestRoad();
        return result[0].intValue();
    }

    public List<String> mostSocialCommunity() {
        List<String> mostSocialCommunity = new ArrayList<>();
        Long[] result = graf.countComponentsAndGetLongestRoad();
        Long componentNode = result[2];

        if (componentNode != -1L) {
            for (Long node : graf.getComponentNodes(componentNode)) {
                User user = socialNetwork.findUser(node);
                mostSocialCommunity.add(user.getFirstName() + " " + user.getLastName());
            }
        }

        return mostSocialCommunity;
    }
}