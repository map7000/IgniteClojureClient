(ns ru.mfilatov.igniteclojureclient.app
  (:require [ru.mfilatov.igniteclojureclient.banktest :as banktest]
    [ru.mfilatov.igniteclojureclient.indextest :as indextest]))



(defn -main []
  (indextest/client)
  )