package src.service;

import src.Utils.Graf;
import src.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<User> mostSocialCommunity() {
        List<User> mostSocialCommunity = new ArrayList<>();
        Long[] result = graf.countComponentsAndGetLongestRoad();
        Long componentNode = result[2];

        if (componentNode != -1L) {
            for (Long node : graf.getComponentNodes(componentNode)) {
                Optional<User> user = socialNetwork.findUser(node);
                user.ifPresent(mostSocialCommunity::add);
            }
        }

        return mostSocialCommunity;
    }
}