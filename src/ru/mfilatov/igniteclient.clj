(ns ru.mfilatov.igniteclient
  (:import (org.apache.ignite Ignition)
           (org.apache.ignite.configuration IgniteConfiguration BinaryConfiguration CacheConfiguration)
           (org.apache.ignite.spi.discovery.tcp TcpDiscoverySpi)
           (org.apache.ignite.spi.discovery.tcp.ipfinder.vm TcpDiscoveryVmIpFinder)
           (org.apache.ignite.cache CacheAtomicityMode CacheMode)))

(defn ignite-config [servers]
  (let [config (IgniteConfiguration.)
        discovery-spi (TcpDiscoverySpi.)
        ip-finder (TcpDiscoveryVmIpFinder.)
        binary-configuration (BinaryConfiguration.)]
    (.setPeerClassLoadingEnabled config true)
    (.setClientMode config true)
    (.setDiscoverySpi config (.setIpFinder discovery-spi (.setAddresses ip-finder servers)))
    (.setBinaryConfiguration config (.setCompactFooter binary-configuration true))
    config))

(defn cache-config [name]
  (let [config (CacheConfiguration.)]
    (.setName config name)
    (.setAtomicityMode config CacheAtomicityMode/TRANSACTIONAL)
    (.setCacheMode config CacheMode/PARTITIONED)
    config))

(defn -main []
  (let [ignite (Ignition/start (ignite-config (list "10.10.0.250")))
        cache (.getOrCreateCache ignite (cache-config "test"))]
    (.put cache 1 1)
    (.close cache)
    (.close ignite))
  )

