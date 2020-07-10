package com.insigma;

public class Test {
    public static void main(String[] args) {
        String regionalName="浙江省,丽水市,莲都区";
        String[] split = regionalName.split(",");
        String province=split[0];
        String city=split[1];
        String area =split[2];
        String province2=province.replace("省","");
        String city2=city.replace("市","");
        String area2=area.replace("区","");
        area2=area2.replace("市","");
        String newCity=province+city;
        String newArea=city+area;
        String newAreaWithProvince=province+area;
        String newAreaFullName=province+city+area;
        String newCity2=province2+city2;
        String newArea2=city2+area2;
        String newArea3=province2+area2;
        String newAreaFullName2=province2+city2+area2;
        String place="浙江莲都";
        place=place.replace("省","");
        place=place.replace("市","");
        place=place.replace("区","");
        if(city2.contains(place)){
            regionalName=newCity;
        }
        if(area2.contains(place)){
            regionalName=newAreaFullName;
        }
        if(!area2.contains(place)&&!city2.contains(place)){
            if(newCity.contains(place)){
                regionalName=newCity;
            }
            if(newArea2.contains(place)){
                regionalName=newArea;
            }
            if(newArea3.contains(place)){
                regionalName=newAreaWithProvince;
            }
            if(!newArea2.contains(place)&&!newCity2.contains(place)){
                if(newAreaFullName2.contains(place)){
                    regionalName=newAreaFullName;
                }
            }else {
                regionalName=null;
            }
        }
        System.err.println(regionalName);
    }
}
