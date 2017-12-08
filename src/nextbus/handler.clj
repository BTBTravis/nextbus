(ns nextbus.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clj-http.client :as client]
            [cheshire.core :refer :all]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def bus_stops
  [
   {"stop_id" "1773", "route_id" "BLU" }
   {"stop_id" "1774", "route_id" "23" }
   {"stop_id" "1674", "route_id" "BLU" }
   {"stop_id" "1672", "route_id" "23" }
  ]
)


(defroutes app-routes
  ; (GET "/" [] {1 "TEST"})
  ; (GET "/" [] (response { :1 'test'}))
  (GET "/" [] (response
    (map 
      (fn [stop] 
        (conj
        (parse-string (:body (client/get "https://mke-bus.herokuapp.com/get/all/predictions/where" {:query-params stop})))
        stop)
      )
      bus_stops
    )
  ))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults (wrap-json-response app-routes) site-defaults))
