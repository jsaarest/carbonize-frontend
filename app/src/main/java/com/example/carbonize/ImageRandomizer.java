package com.example.carbonize;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.R)
public class ImageRandomizer {

    //Singleton method is used, no need for multiple randomizer objects
    private static ImageRandomizer randomizer = new ImageRandomizer();
    public static ImageRandomizer getInstance(){
        return randomizer;
    }
    //image numbers for apartment related picsum photo ids
    List<Integer> imageSeedApartment = new ArrayList<>(List.of(1029,1031,1040,1048,1054,1065,1076,1078,142,164,188,193,214,221,234,238,259,263,274,283,288,290,299,308,322,369,
            391,398,297,405,410,411,437,448,514,552,57,58,594,622));

    public String getRandomApartmentImage()
    {
        return randomApartmentImageUrl();
    }

    private String randomApartmentImageUrl ()
    /*
     Method to return random image number of a building in Picsum.photos
     to be used as part of the picsum -url
     */
    {
        Random r = new Random();
        int randomImageIndex = r.nextInt(imageSeedApartment.size());
        String randomImageUrlString = imageSeedApartment.get(randomImageIndex).toString();
        return randomImageUrlString;
    }

}
