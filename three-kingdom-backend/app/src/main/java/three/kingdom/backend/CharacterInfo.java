package three.kingdom.backend;

import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Arrays;
import java.util.List;

public class CharacterInfo<E extends Comparable<E>> {
    private TreeNode<General> root;
    private int size;
    private TreeNode<General> nodeMilitaryChief;
    private TreeNode<General> nodeManagementChief;

    public CharacterInfo(General emperor, General militaryChief, General managementChief, General[] generals) {
        this.root = new TreeNode<>(emperor);
        size++;

        nodeMilitaryChief = new TreeNode<>(militaryChief);
        nodeManagementChief = new TreeNode<>(managementChief);
        root.addChild(nodeMilitaryChief);
        root.addChild(nodeManagementChief);
        size += 2;

        // Assign generals to their departments based on their intelligence and strength
        for (General general : generals) {
            if (general.getIntelligence() > general.getStrength()) {
                nodeManagementChief.addChild(new TreeNode<>(general));
            } else {
                nodeMilitaryChief.addChild(new TreeNode<>(general));
            }
            size++;
        }
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Emperor\n");
        sb.append("*** ").append(root.getElement().toString()).append("\n");

        // Print military department
        sb.append("\tMilitary Department");
        sb.append("\n\t** ").append(nodeMilitaryChief.getElement().toString()).append("\n");
        for (TreeNode<General> grandchild : nodeMilitaryChief.getChildren()) {
            sb.append("\t\t* ").append(grandchild.getElement().toString()).append("\n");
        }

        // Print management department
        sb.append("\tManagement Department");
        sb.append("\n\t** ").append(nodeManagementChief.getElement().toString()).append("\n");
        for (TreeNode<General> grandchild : nodeManagementChief.getChildren()) {
            sb.append("\t\t* ").append(grandchild.getElement().toString()).append("\n");
        }

        return sb.toString();
    }

    public List<General> getAllGenerals() {
        List<General> generals = new ArrayList<>();
        collectGenerals(root, generals);
        return generals;
    }

    private void collectGenerals(TreeNode<General> node, List<General> generals) {
        generals.add(node.getElement());
        for (TreeNode<General> child : node.getChildren()) {
            collectGenerals(child, generals);
        }
    }

    // Q2 Soilder Arrangement
    // sort the generals based on the ability
    public void sortGeneralsByAbility(String attribute) {
        List<General> generals = getAllGenerals();
        Comparator<General> comparator = getComparatorByAttribute(attribute);

        Collections.sort(generals, comparator);
        System.out.println("Sorted by " + attribute + ": ");
        for (General general : generals) {
            System.out.println(general);
        }
        System.out.println();
    }

    // search the general with specific ability
    // private General binarySearchGeneralByAbility(String attribute, int ability) {
    // List<General> generals = getAllGenerals();
    // Comparator<General> comparator = getComparatorByAttribute(attribute);

    // Collections.sort(generals, comparator);

    // int index = binarySearch(generals, attribute, ability);
    // if (index != -1) {
    // General foundGeneral = generals.get(index);
    // System.out.println("General with " + attribute + " " + ability + " found:");
    // System.out.println(foundGeneral);
    // return foundGeneral;
    // } else {
    // System.out.println("General not found.");
    // return null;
    // }
    // }

    public Comparator<General> getComparatorByAttribute(String attribute) {
        // Implement the logic to create the appropriate comparator based on the
        // attribute
        // For example:
        switch (attribute) {
            case "strength":
                return Comparator.comparing(General::getStrength);
            case "leadership":
                return Comparator.comparing(General::getLeadership);
            case "intelligence":
                return Comparator.comparing(General::getIntelligence);
            case "politic":
                return Comparator.comparing(General::getPolitic);
            case "hitPoint":
                return Comparator.comparing(General::getHitPoint);
            default:
                throw new IllegalArgumentException("Invalid attribute: " + attribute);
        }
    }

    public static int binarySearch(List<General> generals, String attribute, int ability) {
        int low = 0;
        int high = generals.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            General currentGeneral = generals.get(mid);
            int currentAbility = getAttributeByString(currentGeneral, attribute);

            if (currentAbility == ability) {
                return mid;
            } else if (currentAbility < ability) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1; // General not found
    }

    public static int getAttributeByString(General general, String attribute) {
        switch (attribute) {
            case "strength":
                return general.getStrength();
            case "leadership":
                return general.getLeadership();
            case "intelligence":
                return general.getIntelligence();
            case "politic":
                return general.getPolitic();
            case "hitPoint":
                return general.getHitPoint();
            default:
                return 0;
        }
    }

    // search generals based on level
    // public List<General> searchGeneralsByLevel(String attribute, String level) {
    //     List<General> generals = getAllGenerals();
    //     List<General> filteredGenerals = new ArrayList<>();

    //     // Sort the generals in descending order based on the specified attribute
    //     Comparator<General> comparator = getComparatorByAttribute(attribute);
    //     generals.sort(comparator.reversed());

    //     int startIndex = 0;
    //     int endIndex = 2; // Consider the first three generals

    //     // Iterate over the generals and calculate the sum of abilities for each set of
    //     // three generals
    //     while (endIndex < generals.size()) {
    //         int sumAbilities = 0;

    //         // Calculate the sum of abilities for the current set of three generals
    //         for (int i = startIndex; i <= endIndex; i++) {
    //             sumAbilities += generals.get(i).getAbility(attribute);
    //         }

    //         // Assign the generals to their respective levels based on the sum of abilities
    //         if (sumAbilities >= 250 && level.equals("S")) {
    //             for (int i = startIndex; i <= endIndex; i++) {
    //                 filteredGenerals.add(generals.get(i));
    //             }
    //         } else if (sumAbilities >= 220 && sumAbilities < 250 && level.equals("A")) {
    //             for (int i = startIndex; i <= endIndex; i++) {
    //                 filteredGenerals.add(generals.get(i));
    //             }
    //         } else if (sumAbilities >= 190 && sumAbilities < 220 && level.equals("B")) {
    //             for (int i = startIndex; i <= endIndex; i++) {
    //                 filteredGenerals.add(generals.get(i));
    //             }
    //         } else if (sumAbilities <= 190 && level.equals("C")) {
    //             for (int i = startIndex; i <= endIndex; i++) {
    //                 filteredGenerals.add(generals.get(i));
    //             }
    //         }
    //         // Move to the next set of three generals
    //         startIndex += 3;
    //         endIndex += 3;
    //     }
    //     return filteredGenerals;
    // }

    // public void printGeneralsByLevel(String attribute, String level) {

    //     List<General> generals = searchGeneralsByLevel(attribute, level);

    //     System.out.println("Generals in the " + attribute + " field at " + level + "level:");
    //     if (generals.isEmpty()) {
    //         System.out.println("No combination of generals");
    //     } else {
    //         for (int i = 0; i < Math.min(3, generals.size()); i++) {
    //             System.out.println(generals.get(i).getName());
    //         }
    //     }
    //     System.out.println();
    // }

    @RestController
    @RequestMapping("/api")
    @CrossOrigin(origins = "http://localhost:3000", methods = { RequestMethod.GET,
            RequestMethod.POST }, allowedHeaders = "*")
    public static class CharacterInfoController {

        private final List<General> generals;
        private final CharacterInfo<String> characterInfo;

        public CharacterInfoController() {
            generals = createGenerals(); // Initialize the generals' data
            characterInfo = createCharacterInfo();
        }

        private List<General> createGenerals() {
            List<General> generals = new ArrayList<>();

            General emperor = new General("Sun Quan", "sunquan.jpg", "Cavalry", 96, 98, 72, 77, 95);
            General militaryChief = new General("Zhou Yu", "zhouyu.jpg", "Cavalry", 80, 86, 97, 80, 90);
            General managementChief = new General("Zhang Zhao", "zhangzhao.jpg", "Archer", 22, 80, 89, 99, 60);

            General[] generalsArray = {
                    new General("Xu Sheng", "xusheng.jpg", "Archer", 90, 78, 72, 40, 94),
                    new General("Zhu Ge Jin", "zhugejin.jpg", "Archer", 63, 61, 88, 82, 71),
                    new General("Lu Su", "lusu.jpg", "Infantry", 43, 87, 84, 88, 53),
                    new General("Tai Shi Ci", "taishici.jpg", "Cavalry", 96, 81, 43, 33, 97),
                    new General("Xiao Qiao", "xiaoqiao.jpg", "Infantry", 42, 52, 89, 77, 34),
                    new General("Da Qiao", "daqiao.jpg", "Cavalry", 39, 62, 90, 62, 41),
                    new General("Zhou Tai", "zhoutai.jpg", "Infantry", 92, 89, 72, 43, 99),
                    new General("Gan Ning", "ganning.jpg", "Archer", 98, 92, 45, 23, 97),
                    new General("Lu Meng", "lumeng.jpg", "Cavalry", 70, 77, 93, 83, 88),
                    new General("Huang Gai", "huanggai.jpg", "Infantry", 83, 98, 72, 42, 89)
            };

            generals.add(emperor);
            generals.add(militaryChief);
            generals.add(managementChief);
            generals.addAll(Arrays.asList(generalsArray));

            return generals;
        }

        private CharacterInfo<String> createCharacterInfo() {
            General emperor = generals.get(0);
            General militaryChief = generals.get(1);
            General managementChief = generals.get(2);

            General[] generalsArray = generals.subList(3, generals.size()).toArray(new General[0]);

            return new CharacterInfo<>(emperor, militaryChief, managementChief, generalsArray);
        }

        @GetMapping("/generals")
        public List<General> getAllGenerals() {
            return generals;
        }

        @GetMapping("/hierarchical-diagram")
        public String getHierarchicalDiagram() {
            return characterInfo.toString();
        }

        @GetMapping("/generals/sortedBy/{attribute}")
        public List<General> getGeneralsSortedByAttribute(@PathVariable String attribute) {
            List<General> sortedGenerals = new ArrayList<>(generals);
            Comparator<General> comparator = characterInfo.getComparatorByAttribute(attribute);

            sortedGenerals.sort(comparator);

            return sortedGenerals;
        }

        @GetMapping("/generals/binarySearch/{attribute}/{ability}")
        public ResponseEntity<General> binarySearchGeneralByAbility(@PathVariable String attribute,
                @PathVariable int ability) {
            List<General> generals = getAllGenerals();
            Comparator<General> comparator = characterInfo.getComparatorByAttribute(attribute);

            Collections.sort(generals, comparator);

            int index = binarySearch(generals, attribute, ability);
            if (index != -1) {
                General foundGeneral = generals.get(index);
                System.out.println("General with " + attribute + " " + ability + " found:");
                System.out.println(foundGeneral);
                return ResponseEntity.ok(foundGeneral);
            } else {
                System.out.println("General not found.");
                return ResponseEntity.notFound().build();
            }
        }

        @GetMapping("/generals/suggestions/{attribute}/{level}")
        public ResponseEntity<List<General>> getGeneralSuggestions(
                @PathVariable String attribute,
                @PathVariable String level) {

            List<General> suggestions = searchGeneralsByLevel(attribute, level);

            if (!suggestions.isEmpty()) {
                return ResponseEntity.ok(suggestions);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        // search generals based on level
        public List<General> searchGeneralsByLevel(String attribute, String level) {
            List<General> generals = getAllGenerals();
            List<General> filteredGenerals = new ArrayList<>();

            // Sort the generals in descending order based on the specified attribute
            Comparator<General> comparator = characterInfo.getComparatorByAttribute(attribute);
            generals.sort(comparator.reversed());

            int startIndex = 0;
            int endIndex = 2; // Consider the first three generals

            // Iterate over the generals and calculate the sum of abilities for each set of
            // three generals
            while (endIndex < generals.size()) {
                int sumAbilities = 0;

                // Calculate the sum of abilities for the current set of three generals
                for (int i = startIndex; i <= endIndex; i++) {
                    sumAbilities += generals.get(i).getAttributeValue(attribute);
                }

                // Assign the generals to their respective levels based on the sum of abilities
                if (sumAbilities >= 250 && level.equals("S")) {
                    for (int i = startIndex; i <= endIndex; i++) {
                        filteredGenerals.add(generals.get(i));
                        if (filteredGenerals.size() >= 3) {
                            break;
                        }
                    }
                } else if (sumAbilities >= 220 && sumAbilities < 250 && level.equals("A")) {
                    for (int i = startIndex; i <= endIndex; i++) {
                        filteredGenerals.add(generals.get(i));
                        if (filteredGenerals.size() >= 3) {
                            break;
                        }
                    }
                } else if (sumAbilities >= 190 && sumAbilities < 220 && level.equals("B")) {
                    for (int i = startIndex; i <= endIndex; i++) {
                        filteredGenerals.add(generals.get(i));
                        if (filteredGenerals.size() >= 3) {
                            break;
                        }
                    }
                } else if (sumAbilities <= 190 && level.equals("C")) {
                    for (int i = startIndex; i <= endIndex; i++) {
                        filteredGenerals.add(generals.get(i));
                        if (filteredGenerals.size() >= 3) {
                            break;
                        }
                    }
                }

                // Move to the next set of three generals
                startIndex += 3;
                endIndex += 3;

                // Check if the maximum number of generals has been reached
                if (filteredGenerals.size() >= 3) {
                    break;
                }
            }
            return filteredGenerals;
        }

        public void printGeneralsByLevel(String attribute, String level) {

            List<General> generals = searchGeneralsByLevel(attribute, level);

            System.out.println("Generals in the " + attribute + " field at " + level + "level:");
            if (generals.isEmpty()) {
                System.out.println("No combination of generals");
            } else {
                for (int i = 0; i < Math.min(3, generals.size()); i++) {
                    System.out.println(generals.get(i).getName());
                }
            }
            System.out.println();
        }
    }

}

@Configuration
class CorsConfig1 implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
