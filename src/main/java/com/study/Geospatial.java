package com.study;

import java.util.List;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.params.GeoSearchParam;
import redis.clients.jedis.resps.GeoRadiusResponse;

public class Geospatial {

    public static void main(String[] args) {
        try (var jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.geoadd("stores2:geo", 127.098942, 37.514960, "Station");
                jedis.geoadd("stores2:geo", 127.101270, 37.514449, "Bookstore");
                // "GEOADD" "stores2:geo" "127.098942" "37.51496" "Station"
                // "GEOADD" "stores2:geo" "127.10127" "37.514449" "Bookstore"

                Double geodist = jedis.geodist("stores2:geo", "Station", "Bookstore");
                System.out.println(geodist); // 213.0738 / 213m

                List<GeoRadiusResponse> responseList = jedis.geosearch("stores2:geo",
                    new GeoSearchParam()
                        .fromLonLat(new GeoCoordinate(127.095, 37.512))
                        .byRadius(500, GeoUnit.M)
                        .withCoord()
                );

                responseList.forEach(response -> {
                    System.out.printf("%s %f %f", response.getMemberByString(),
                        response.getCoordinate().getLatitude(),
                        response.getCoordinate().getLongitude());
                    // Station 37.514959 127.098944
                    // "GEOSEARCH" "stores2:geo" "FROMLONLAT" "127.095" "37.512" "BYRADIUS" "500.0" "m" "WITHCOORD"
                });
            }
        }
    }

}
