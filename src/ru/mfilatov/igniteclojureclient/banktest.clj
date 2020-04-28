(ns ru.mfilatov.igniteclojureclient.banktest
  (:require [ru.mfilatov.igniteclojureclient.core :as core])
  (:import (org.apache.ignite Ignition)))

(use 'clojure.tools.logging)

(def accounts 10)
(def account-balance 100)
(def addresses (list "10.10.0.250"))
(def cache-name "ACCOUNTS")
(def amount 3)

(defn init-cache [cache]
  (dotimes [i accounts]
    (error "Creating account" i)
    (.put cache i account-balance)))

(defn read [cache]
  (.getAll cache (set (range 0 10))))

(defn transfer-money [from to cache]
  (let [b1 (- (.get cache from) amount)
        b2 (+ (.get cache to) amount)]
    (.put cache from b1)
    (.put cache to b2)))

(defn client []
  (let [conn (Ignition/start (core/ignite-config addresses))
        cache (.getOrCreateCache conn (core/cache-config cache-name))]
    (init-cache cache)
    (error (.values (read cache)))
    (transfer-money 1 2 cache)
    (error (.values (read cache)))
    (.close cache)
    (.close conn)))

