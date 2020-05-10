(ns ru.mfilatov.igniteclojureclient.core
    (:import
      (org.apache.ignite.configuration IgniteConfiguration BinaryConfiguration CacheConfiguration)
      (org.apache.ignite.spi.discovery.tcp TcpDiscoverySpi)
      (org.apache.ignite.spi.discovery.tcp.ipfinder.vm TcpDiscoveryVmIpFinder)
      (org.apache.ignite.cache CacheAtomicityMode CacheMode CacheWriteSynchronizationMode)))

(def cache-mode CacheMode/PARTITIONED)
(def cache-atomicity-mode CacheAtomicityMode/TRANSACTIONAL)
(def cache-write-sync-mode CacheWriteSynchronizationMode/FULL_SYNC)
(def read-from-backups true)
(def backups 2)

(defn ignite-config [servers]
      (let [config (IgniteConfiguration.)
            ;discovery-spi (TcpDiscoverySpi.)
            ip-finder (TcpDiscoveryVmIpFinder.)
            binary-configuration (BinaryConfiguration.)]
           (.setPeerClassLoadingEnabled config true)
           (.setClientMode config true)
           (.setDiscoverySpi config (.setIpFinder (TcpDiscoverySpi.) (.setAddresses ip-finder servers)))
           (.setBinaryConfiguration config (.setCompactFooter binary-configuration true))
           config))

(defn cache-config [name]
      (let [config (CacheConfiguration.)]
           (.setName config name)
           (.setAtomicityMode config cache-atomicity-mode)
           (.setCacheMode config cache-mode)
           (.setBackups config backups)
           (.setReadFromBackup config read-from-backups)
           (.setWriteSynchronizationMode config cache-write-sync-mode)
           config))