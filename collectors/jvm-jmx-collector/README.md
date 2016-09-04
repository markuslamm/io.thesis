## jvm-jmx-collector

Collects default JVM data provided by JMX and exports the result:

```json
{
    "clientTimestamp": "2016-08-12T20:00:11.294",
    "clientHost": "127.0.1.1",
    "clientPort": 9091,
    "instanceId": "collector-client:42670a21f012e4b178678fd9eba76eed",
    "collectorType": "jvm_jmx",
    "data": {
        "nio-buffer-pools": [
            {
                "name": "mapped",
                "count": 0,
                "memoryUsed": 0,
                "totalCapacity": 0
            },
            {
                "name": "direct",
                "count": 13,
                "memoryUsed": 267272,
                "totalCapacity": 267271
            }
        ],
        "memory": {
            "objectPendingFinalizationCount": 0,
            "heapMemoryUsage": {
                "committed": 257425408,
                "init": 268435456,
                "max": 257425408,
                "used": 82003104
            },
            "nonHeapMemoryUsage": {
                "committed": 50724864,
                "init": 2555904,
                "max": -1,
                "used": 49611960
            }
        },
        "memory-pools": [
            {
                "name": "Metaspace",
                "type": "NON_HEAP",
                "valid": true,
                "collectionUsage": null,
                "usage": {
                    "committed": 33685504,
                    "init": 0,
                    "max": -1,
                    "used": 32946576
                },
                "peakUsage": {
                    "committed": 33685504,
                    "init": 0,
                    "max": -1,
                    "used": 32946576
                },
                "memoryManagerNames": [
                    "Metaspace Manager"
                ],
                "usageThreshold": 0,
                "usageCount": 0,
                "usageExceeded": false
            },
            {
                "name": "PS Old Gen",
                "type": "HEAP",
                "valid": true,
                "collectionUsage": {
                    "committed": 179306496,
                    "init": 179306496,
                    "max": 179306496,
                    "used": 4492064
                },
                "usage": {
                    "committed": 179306496,
                    "init": 179306496,
                    "max": 179306496,
                    "used": 4508448
                },
                "peakUsage": {
                    "committed": 179306496,
                    "init": 179306496,
                    "max": 179306496,
                    "used": 4508448
                },
                "collectionThreshold": 0,
                "collectionCount": 0,
                "collectionExceeded": false,
                "memoryManagerNames": [
                    "PS MarkSweep"
                ],
                "usageThreshold": 0,
                "usageCount": 0,
                "usageExceeded": false
            },
            {
                "name": "PS Eden Space",
                "type": "HEAP",
                "valid": true,
                "collectionUsage": {
                    "committed": 67108864,
                    "init": 67108864,
                    "max": 67108864,
                    "used": 0
                },
                "usage": {
                    "committed": 67108864,
                    "init": 67108864,
                    "max": 67108864,
                    "used": 929936
                },
                "peakUsage": {
                    "committed": 67108864,
                    "init": 67108864,
                    "max": 67108864,
                    "used": 67108864
                },
                "collectionThreshold": 0,
                "collectionCount": 0,
                "collectionExceeded": false,
                "memoryManagerNames": [
                    "PS MarkSweep",
                    "PS Scavenge"
                ]
            },
            {
                "name": "Compressed Class Space",
                "type": "NON_HEAP",
                "valid": true,
                "collectionUsage": null,
                "usage": {
                    "committed": 4587520,
                    "init": 0,
                    "max": 1073741824,
                    "used": 4362984
                },
                "peakUsage": {
                    "committed": 4587520,
                    "init": 0,
                    "max": 1073741824,
                    "used": 4362984
                },
                "memoryManagerNames": [
                    "Metaspace Manager"
                ],
                "usageThreshold": 0,
                "usageCount": 0,
                "usageExceeded": false
            },
            {
                "name": "Code Cache",
                "type": "NON_HEAP",
                "valid": true,
                "collectionUsage": null,
                "usage": {
                    "committed": 12451840,
                    "init": 2555904,
                    "max": 251658240,
                    "used": 12305152
                },
                "peakUsage": {
                    "committed": 12451840,
                    "init": 2555904,
                    "max": 251658240,
                    "used": 12316032
                },
                "memoryManagerNames": [
                    "CodeCacheManager"
                ],
                "usageThreshold": 0,
                "usageCount": 0,
                "usageExceeded": false
            },
            {
                "name": "PS Survivor Space",
                "type": "HEAP",
                "valid": true,
                "collectionUsage": {
                    "committed": 11010048,
                    "init": 11010048,
                    "max": 11010048,
                    "used": 3178816
                },
                "usage": {
                    "committed": 11010048,
                    "init": 11010048,
                    "max": 11010048,
                    "used": 3178816
                },
                "peakUsage": {
                    "committed": 11010048,
                    "init": 11010048,
                    "max": 11010048,
                    "used": 10986432
                },
                "collectionThreshold": 0,
                "collectionCount": 0,
                "collectionExceeded": false,
                "memoryManagerNames": [
                    "PS MarkSweep",
                    "PS Scavenge"
                ]
            }
        ],
        "operating-system": {
            "arch": "amd64",
            "availableProcessors": 2,
            "name": "Linux",
            "systemLoadAvg": 1.5654296875,
            "version": "4.4.0-34-generic",
            "processCpuLoad": 0.054600606673407485,
            "processCpuTime": 41140000000,
            "committedVirtualMemorySize": 1794162688,
            "systemCpuLoad": 0.22267206477732793,
            "totalPhysicalMemorySize": 8280924160,
            "freePhysicalMemorySize": 944017408,
            "totalSwapSpaceSize": 8498704384,
            "freeSwapSpaceSize": 8273494016,
            "maxFileDescriptorCount": 65536,
            "openFileDescriptorCount": 68
        },
        "runtime": {
            "startTime": 1471022078735,
            "uptime": 2732538,
            "specName": "Java Virtual Machine Specification",
            "specVendor": "Oracle Corporation",
            "specVersion": "1.8",
            "vmName": "OpenJDK 64-Bit Server VM",
            "vmVendor": "Oracle Corporation",
            "vmVersion": "25.92-b14"
        },
        "threads": {
            "threadCount": 39,
            "daemonThreadCount": 33,
            "peakThreadCount": 48,
            "totalStartedThreadCount": 93,
            "currentThreadUserTime": 0,
            "currentThreadCpuTime": 7828358
        },
        "classloading": {
            "loaded": 5700,
            "totalLoaded": 5700,
            "unloaded": 0
        },
        "garbage-collectors": [
            {
                "name": "PS MarkSweep",
                "collectionCount": 1,
                "collectionTime": 71,
                "memoryPoolNames": [
                    "PS Eden Space",
                    "PS Survivor Space",
                    "PS Old Gen"
                ],
                "valid": true,
                "lastGcInfo": {
                    "starttime": 7761,
                    "endtime": 7832,
                    "duration": 71,
                    "memoryUsageBeforeGc": [
                        {
                            "name": "PS Eden Space",
                            "committed": 67108864,
                            "init": 67108864,
                            "max": 67108864,
                            "used": 0
                        },
                        {
                            "name": "Code Cache",
                            "committed": 4325376,
                            "init": 2555904,
                            "max": 251658240,
                            "used": 4274816
                        },
                        {
                            "name": "Compressed Class Space",
                            "committed": 3145728,
                            "init": 0,
                            "max": 1073741824,
                            "used": 3011760
                        },
                        {
                            "name": "PS Survivor Space",
                            "committed": 11010048,
                            "init": 11010048,
                            "max": 11010048,
                            "used": 4063408
                        },
                        {
                            "name": "PS Old Gen",
                            "committed": 179306496,
                            "init": 179306496,
                            "max": 179306496,
                            "used": 3886136
                        },
                        {
                            "name": "Metaspace",
                            "committed": 21757952,
                            "init": 0,
                            "max": -1,
                            "used": 21418112
                        }
                    ],
                    "memoryUsageAfterGc": [
                        {
                            "name": "PS Eden Space",
                            "committed": 67108864,
                            "init": 67108864,
                            "max": 67108864,
                            "used": 0
                        },
                        {
                            "name": "Code Cache",
                            "committed": 4325376,
                            "init": 2555904,
                            "max": 251658240,
                            "used": 4274816
                        },
                        {
                            "name": "Compressed Class Space",
                            "committed": 3145728,
                            "init": 0,
                            "max": 1073741824,
                            "used": 3011760
                        },
                        {
                            "name": "PS Survivor Space",
                            "committed": 11010048,
                            "init": 11010048,
                            "max": 11010048,
                            "used": 0
                        },
                        {
                            "name": "PS Old Gen",
                            "committed": 179306496,
                            "init": 179306496,
                            "max": 179306496,
                            "used": 4492064
                        },
                        {
                            "name": "Metaspace",
                            "committed": 21757952,
                            "init": 0,
                            "max": -1,
                            "used": 21418112
                        }
                    ]
                }
            },
            {
                "name": "PS Scavenge",
                "collectionCount": 4,
                "collectionTime": 204,
                "memoryPoolNames": [
                    "PS Eden Space",
                    "PS Survivor Space"
                ],
                "valid": true,
                "lastGcInfo": {
                    "starttime": 2732454,
                    "endtime": 2732471,
                    "duration": 17,
                    "memoryUsageBeforeGc": [
                        {
                            "name": "PS Eden Space",
                            "committed": 67108864,
                            "init": 67108864,
                            "max": 67108864,
                            "used": 67108864
                        },
                        {
                            "name": "Code Cache",
                            "committed": 12451840,
                            "init": 2555904,
                            "max": 251658240,
                            "used": 12303168
                        },
                        {
                            "name": "Compressed Class Space",
                            "committed": 4587520,
                            "init": 0,
                            "max": 1073741824,
                            "used": 4362984
                        },
                        {
                            "name": "PS Survivor Space",
                            "committed": 11010048,
                            "init": 11010048,
                            "max": 11010048,
                            "used": 10732896
                        },
                        {
                            "name": "PS Old Gen",
                            "committed": 179306496,
                            "init": 179306496,
                            "max": 179306496,
                            "used": 4500256
                        },
                        {
                            "name": "Metaspace",
                            "committed": 33685504,
                            "init": 0,
                            "max": -1,
                            "used": 32946576
                        }
                    ],
                    "memoryUsageAfterGc": [
                        {
                            "name": "PS Eden Space",
                            "committed": 67108864,
                            "init": 67108864,
                            "max": 67108864,
                            "used": 0
                        },
                        {
                            "name": "Code Cache",
                            "committed": 12451840,
                            "init": 2555904,
                            "max": 251658240,
                            "used": 12303168
                        },
                        {
                            "name": "Compressed Class Space",
                            "committed": 4587520,
                            "init": 0,
                            "max": 1073741824,
                            "used": 4362984
                        },
                        {
                            "name": "PS Survivor Space",
                            "committed": 11010048,
                            "init": 11010048,
                            "max": 11010048,
                            "used": 3178816
                        },
                        {
                            "name": "PS Old Gen",
                            "committed": 179306496,
                            "init": 179306496,
                            "max": 179306496,
                            "used": 4508448
                        },
                        {
                            "name": "Metaspace",
                            "committed": 33685504,
                            "init": 0,
                            "max": -1,
                            "used": 32946576
                        }
                    ]
                }
            }
        ]
    }
}
```