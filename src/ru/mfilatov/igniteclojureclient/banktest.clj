(ns ru.mfilatov.igniteclojureclient.banktest
    (:require [ru.mfilatov.igniteclojureclient.core :as core])
    (:import (org.apache.ignite Ignition)
      (org.apache.ignite.transactions TransactionConcurrency TransactionIsolation)))

(use 'clojure.tools.logging)

(def accounts 10)
(def account-balance 100)
(def addresses (list "10.10.0.250"))
(def cache-name "ACCOUNTS")
(def amount 3)
(def n 10)

(def transaction-concurrency TransactionConcurrency/OPTIMISTIC)
(def transaction-isolation TransactionIsolation/SERIALIZABLE)



(defn init-cache [cache]
      (dotimes [i accounts]
               (error "Creating account" i)
               (.put cache i account-balance)))

(defn read-values [cache n]
      (vals (.getAll cache (set (range 0 n)))))

(defn transfer-money [from to cache]
      (let [b1 (- (.get cache from) amount)
            b2 (+ (.get cache to) amount)]
           (.put cache from b1)
           (.put cache to b2)))

(defn read-values-tr [ignite cache n]
      (with-open [tr (.txStart (.transactions ignite) transaction-concurrency transaction-isolation)]
                 (let [values (read-values cache n)]
                      (.commit tr)
                      values)))

(defn transfer-money-tr [ignite from to cache]
      (with-open [tr (.txStart (.transactions ignite) transaction-concurrency transaction-isolation)]
                 (transfer-money from to cache)
                 (.commit tr)))

(defn client []
      (let [ignite (Ignition/start (core/ignite-config addresses))
            cache (.getOrCreateCache ignite (core/cache-config cache-name))]
           (init-cache cache)
           (dotimes [i 10]
                    (error (read-values-tr ignite cache n))
                    (error (reduce + (read-values-tr ignite cache n)))
                    (transfer-money-tr ignite (long (rand n)) (long (rand n)) cache))
           (.destroy cache)
           (.close ignite)))

