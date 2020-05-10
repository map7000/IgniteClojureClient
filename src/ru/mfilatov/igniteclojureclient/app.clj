(ns ru.mfilatov.igniteclojureclient.app
  (:require [ru.mfilatov.igniteclojureclient.banktest :as banktest]))



(defn -main []
  (banktest/client)
  )