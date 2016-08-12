## flink-jmx-collector

Collects Apache Flink's JMX metrics available since version 1.1.0 and exports the result:

```json
{
    "clientTimestamp": "2016-08-12T20:14:01.485",
    "clientHost": "127.0.1.1",
    "clientPort": 9091,
    "instanceId": "collector-client:2a8251cdb3ba6cab9ec631a7b0d0683e",
    "collectorType": "flink_jmx",
    "data": {
        "jobmanager": [
            {
                "host": "172.18.0.3",
                "taskSlotsTotal": 2,
                "numRegisteredTaskManagers": 1,
                "numRunningJobs": 0,
                "taskSlotsAvailable": 2,
                "jvm": {
                    "cpu": {
                        "Load": 0.375,
                        "Time": 56010000000
                    },
                    "classLoader": {
                        "ClassesUnloaded": 36,
                        "ClassesLoaded": 6518
                    },
                    "threads": {
                        "Count": 41
                    },
                    "memory": [
                        {
                            "name": "Mapped",
                            "Count": 0,
                            "TotalCapacity": 0,
                            "MemoryUsed": 0
                        },
                        {
                            "name": "NonHeap",
                            "Committed": 59006976,
                            "Max": -1,
                            "Used": 57767976
                        },
                        {
                            "name": "Heap",
                            "Committed": 253231104,
                            "Max": 253231104,
                            "Used": 74061488
                        },
                        {
                            "name": "Direct",
                            "MemoryUsed": 307464,
                            "TotalCapacity": 307463,
                            "Count": 15
                        }
                    ],
                    "garbageCollector": [
                        {
                            "name": "PS_Scavenge",
                            "Count": 5,
                            "Time": 229
                        },
                        {
                            "name": "PS_MarkSweep",
                            "Time": 166,
                            "Count": 2
                        }
                    ]
                }
            }
        ]
    }
}
```