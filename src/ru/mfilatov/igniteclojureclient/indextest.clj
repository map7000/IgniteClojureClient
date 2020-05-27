(ns ru.mfilatov.igniteclojureclient.indextest
    (:require [ru.mfilatov.igniteclojureclient.core :as core])
    (:import (org.apache.ignite Ignition) (org.apache.ignite.cache.query SqlFieldsQuery)))

(use 'clojure.tools.logging)

(def addresses (into-array (list "45.143.94.146")))
(def cache-name "TRANSACTIONS")


(defn client []
      (let [ignite (Ignition/startClient (core/client-config addresses))]
           (info "Connected")
           (.getAll (.query ignite
                            (SqlFieldsQuery. "CREATE TABLE IF NOT EXISTS PERSON (id int PRIMARY KEY, name varchar)")))
           (.getAll (.query ignite
                           (SqlFieldsQuery. "CREATE INDEX IF NOT EXISTS NAME_IDX ON PERSON (name) INLINE_SIZE 48")))
           (.getAll (.query ignite
                            (SqlFieldsQuery. "INSERT INTO PERSON(id, name) VALUES(1, 3)")))
           (info
             (.next (.iterator
                      (.next (.iterator
                               (.getAll (.query ignite
                                                (SqlFieldsQuery. "SELECT name from PERSON WHERE id=1"))))))))
           (.getAll (.query ignite
                            (SqlFieldsQuery. "UPDATE PERSON SET NAME = 'Misha' WHERE id = 1")))
           (info
             (.next (.iterator
                      (.next (.iterator
                               (.getAll (.query ignite
                                                (SqlFieldsQuery. "SELECT name from PERSON WHERE id=1"))))))))
           (.getAll (.query ignite
                            (SqlFieldsQuery. "DROP TABLE IF EXISTS PERSON")))
           (.close ignite)))