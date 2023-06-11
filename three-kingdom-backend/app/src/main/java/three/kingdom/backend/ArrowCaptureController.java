package three.kingdom.backend;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", methods = { RequestMethod.OPTIONS,
    RequestMethod.POST }, allowedHeaders = "*")
public class ArrowCaptureController {

    @PostMapping("/calculate")
    public ArrowCaptureResponse calculateArrows(@RequestBody ArrowCaptureRequest request) {
        int[] strawMenNumber = request.getStrawMenNumber();
        int[] arrowNumber = request.getArrowNumber();

        BoatSide[] sides = new BoatSide[4];
        sides[0] = new BoatSide("front", strawMenNumber[0]);
        sides[1] = new BoatSide("left", strawMenNumber[1]);
        sides[2] = new BoatSide("right", strawMenNumber[2]);
        sides[3] = new BoatSide("back", strawMenNumber[3]);

        int index = 0;
        int[][] result = new int[arrowNumber.length][2];
        while (index < arrowNumber.length) {
            int maxArrow = -1;
            int sideIndex = -1;
            for (int i = 0; i < 4; i++) {
                if (sides[i].getNumberOfArrowsCaptured(arrowNumber[index]) > maxArrow) {
                    sideIndex = i;
                    maxArrow = sides[i].getNumberOfArrowsCaptured(arrowNumber[index]);
                }
            }
            result[index][0] = sideIndex;
            result[index][1] = maxArrow;
            sides[sideIndex].captureArrow(arrowNumber[index]);
            index++;
        }

        int total = getTotal(result);

        ArrowCaptureResponse response = new ArrowCaptureResponse();
        response.setBoatDirection(getBoatDirection(result, sides));
        response.setArrowReceived(getArrowReceived(result));
        response.setTotal(total);

        return response;
    }

    private String[] getBoatDirection(int[][] result, BoatSide[] sides) {
        String[] boatDirection = new String[result.length];
        for (int i = 0; i < result.length; i++) {
            boatDirection[i] = sides[result[i][0]].toString();
        }
        return boatDirection;
    }

    private int[] getArrowReceived(int[][] result) {
        int[] arrowReceived = new int[result.length];
        for (int i = 0; i < result.length; i++) {
            arrowReceived[i] = result[i][1];
        }
        return arrowReceived;
    }

    private int getTotal(int[][] result) {
        int total = 0;
        for (int i = 0; i < result.length; i++) {
            total += result[i][1];
        }
        return total;
    }

}

class BoatSide {

    private String side;
    private int strawMan;
    private int captureTimes;

    public BoatSide(String side, int strawMan) {
        this.side = side;
        this.strawMan = strawMan;
        this.captureTimes = 0;
    }

    public double getEfficiency() {
        switch (captureTimes) {
            case 0:
                return 1;
            case 1:
                return 0.8;
            case 2:
                return 0.4;
        }
        return 0;
    }

    public int getNumberOfArrowsCaptured(int numberOfArrows) {
        return (int) ((double) this.strawMan / 100 * getEfficiency() * numberOfArrows);
    }

    public void captureArrow(int numberOfArrows) {
        this.captureTimes++;
    }

    @Override
    public String toString() {
        return this.side;
    }

}

class ArrowCaptureRequest {

    private int[] strawMenNumber;
    private int[] arrowNumber;

    public int[] getStrawMenNumber() {
        return strawMenNumber;
    }

    public void setStrawMenNumber(int[] strawMenNumber) {
        this.strawMenNumber = strawMenNumber;
    }

    public int[] getArrowNumber() {
        return arrowNumber;
    }

    public void setArrowNumber(int[] arrowNumber) {
        this.arrowNumber = arrowNumber;
    }
}

class ArrowCaptureResponse {

    private String[] boatDirection;
    private int[] arrowReceived;
    private int total;

    public String[] getBoatDirection() {
        return boatDirection;
    }

    public void setBoatDirection(String[] boatDirection) {
        this.boatDirection = boatDirection;
    }

    public int[] getArrowReceived() {
        return arrowReceived;
    }

    public void setArrowReceived(int[] arrowReceived) {
        this.arrowReceived = arrowReceived;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}


@Configuration
class CorsConfig3 implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}