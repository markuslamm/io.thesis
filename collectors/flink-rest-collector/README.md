## flink-rest-collector

Collects data provided by Apache Flink's HTTP monitoring API and exports the result:

```json
{
    "clientTimestamp": "2016-08-12T20:02:53.15",
    "clientHost": "127.0.1.1",
    "clientPort": 9091,
    "instanceId": "collector-client:3ef6658ea9219cd0c4185c6512b8e356",
    "collectorType": "flink_rest",
    "data": {
        "cluster": {
            "timezone-name": "Greenwich Mean Time",
            "timezone-offset": 0,
            "version": "1.1.0",
            "revision": "45f7825 @ 02.08.2016 @ 18:30:53 UTC",
            "taskManagers": 1,
            "slots-total": 2,
            "slots-available": 2,
            "jobs-running": 0,
            "jobs-finished": 1,
            "jobs-cancelled": 0,
            "jobs-failed": 0
        },
        "jobs": [
            {
                "duration": 2074,
                "endTime": 1471024935012,
                "exceptions": {
                    "truncated": false,
                    "rootException": null,
                    "allExceptions": []
                },
                "executionMode": "PIPELINED",
                "isStoppable": false,
                "jobParallelism": 1,
                "name": "Flink Java Job at Fri Aug 12 18:02:12 GMT 2016",
                "now": 1471024973138,
                "objectReuseMode": false,
                "plan": {
                    "jid": "3f3d62d2995c02bea0aa71cd4e4163bb",
                    "name": "Flink Java Job at Fri Aug 12 18:02:12 GMT 2016",
                    "nodes": [
                        {
                            "id": "acdb73c5f4dd53d60b11c814b1512900",
                            "parallelism": 1,
                            "operator": "Data Source -> FlatMap -> GroupCombine",
                            "operator_strategy": "(none)<br/> -&gt; FlatMap<br/> -&gt; Sorted Combine",
                            "description": "DataSource (at main(WordCount.java:52) (org.apache.flink.api.java.io.CollectionInputFormat))<br/> -&gt; FlatMap (FlatMap at main(WordCount.java:61))<br/> -&gt; Combine(SUM(1), at main(WordCount.java:64)",
                            "optimizer_properties": {
                                "global_properties": [
                                    {
                                        "name": "Partitioning",
                                        "value": "RANDOM_PARTITIONED"
                                    },
                                    {
                                        "name": "Partitioning Order",
                                        "value": "(none)"
                                    },
                                    {
                                        "name": "Uniqueness",
                                        "value": "not unique"
                                    }
                                ],
                                "local_properties": [
                                    {
                                        "name": "Order",
                                        "value": "(none)"
                                    },
                                    {
                                        "name": "Grouping",
                                        "value": "not grouped"
                                    },
                                    {
                                        "name": "Uniqueness",
                                        "value": "not unique"
                                    }
                                ],
                                "estimates": [
                                    {
                                        "name": "Est. Output Size",
                                        "value": "(unknown)"
                                    },
                                    {
                                        "name": "Est. Cardinality",
                                        "value": "(unknown)"
                                    }
                                ],
                                "costs": [
                                    {
                                        "name": "Network",
                                        "value": "0.0"
                                    },
                                    {
                                        "name": "Disk I/O",
                                        "value": "0.0"
                                    },
                                    {
                                        "name": "CPU",
                                        "value": "0.0"
                                    },
                                    {
                                        "name": "Cumulative Network",
                                        "value": "0.0"
                                    },
                                    {
                                        "name": "Cumulative Disk I/O",
                                        "value": "0.0"
                                    },
                                    {
                                        "name": "Cumulative CPU",
                                        "value": "0.0"
                                    }
                                ],
                                "compiler_hints": [
                                    {
                                        "name": "Output Size (bytes)",
                                        "value": "(none)"
                                    },
                                    {
                                        "name": "Output Cardinality",
                                        "value": "(none)"
                                    },
                                    {
                                        "name": "Avg. Output Record Size (bytes)",
                                        "value": "(none)"
                                    },
                                    {
                                        "name": "Filter Factor",
                                        "value": "(none)"
                                    }
                                ]
                            }
                        },
                        {
                            "id": "65c08171f8232e197e0cca1da43c4968",
                            "parallelism": 1,
                            "operator": "GroupReduce",
                            "operator_strategy": "Sorted Group Reduce",
                            "description": "Reduce (SUM(1), at main(WordCount.java:64)",
                            "inputs": [
                                {
                                    "num": 0,
                                    "id": "acdb73c5f4dd53d60b11c814b1512900",
                                    "ship_strategy": "Hash Partition on [0]",
                                    "local_strategy": "Sort (combining) on [0:ASC]",
                                    "exchange": "pipelined"
                                }
                            ],
                            "optimizer_properties": {
                                "global_properties": [
                                    {
                                        "name": "Partitioning",
                                        "value": "HASH_PARTITIONED"
                                    },
                                    {
                                        "name": "Partitioned on",
                                        "value": "[0]"
                                    },
                                    {
                                        "name": "Partitioning Order",
                                        "value": "(none)"
                                    },
                                    {
                                        "name": "Uniqueness",
                                        "value": "not unique"
                                    }
                                ],
                                "local_properties": [
                                    {
                                        "name": "Order",
                                        "value": "[0:ASC]"
                                    },
                                    {
                                        "name": "Grouped on",
                                        "value": "[0]"
                                    },
                                    {
                                        "name": "Uniqueness",
                                        "value": "not unique"
                                    }
                                ],
                                "estimates": [
                                    {
                                        "name": "Est. Output Size",
                                        "value": "(unknown)"
                                    },
                                    {
                                        "name": "Est. Cardinality",
                                        "value": "(unknown)"
                                    }
                                ],
                                "costs": [
                                    {
                                        "name": "Network",
                                        "value": "(unknown)"
                                    },
                                    {
                                        "name": "Disk I/O",
                                        "value": "(unknown)"
                                    },
                                    {
                                        "name": "CPU",
                                        "value": "(unknown)"
                                    },
                                    {
                                        "name": "Cumulative Network",
                                        "value": "(unknown)"
                                    },
                                    {
                                        "name": "Cumulative Disk I/O",
                                        "value": "(unknown)"
                                    },
                                    {
                                        "name": "Cumulative CPU",
                                        "value": "(unknown)"
                                    }
                                ],
                                "compiler_hints": [
                                    {
                                        "name": "Output Size (bytes)",
                                        "value": "(none)"
                                    },
                                    {
                                        "name": "Output Cardinality",
                                        "value": "(none)"
                                    },
                                    {
                                        "name": "Avg. Output Record Size (bytes)",
                                        "value": "(none)"
                                    },
                                    {
                                        "name": "Filter Factor",
                                        "value": "(none)"
                                    }
                                ]
                            }
                        },
                        {
                            "id": "656fc1e6b1806fc45f7453fd4323e6bf",
                            "parallelism": 1,
                            "operator": "Data Sink",
                            "operator_strategy": "(none)",
                            "description": "DataSink (collect())",
                            "inputs": [
                                {
                                    "num": 0,
                                    "id": "65c08171f8232e197e0cca1da43c4968",
                                    "ship_strategy": "Forward",
                                    "exchange": "pipelined"
                                }
                            ],
                            "optimizer_properties": {
                                "global_properties": [
                                    {
                                        "name": "Partitioning",
                                        "value": "HASH_PARTITIONED"
                                    },
                                    {
                                        "name": "Partitioned on",
                                        "value": "[0]"
                                    },
                                    {
                                        "name": "Partitioning Order",
                                        "value": "(none)"
                                    },
                                    {
                                        "name": "Uniqueness",
                                        "value": "not unique"
                                    }
                                ],
                                "local_properties": [
                                    {
                                        "name": "Order",
                                        "value": "[0:ASC]"
                                    },
                                    {
                                        "name": "Grouped on",
                                        "value": "[0]"
                                    },
                                    {
                                        "name": "Uniqueness",
                                        "value": "not unique"
                                    }
                                ],
                                "estimates": [
                                    {
                                        "name": "Est. Output Size",
                                        "value": "(unknown)"
                                    },
                                    {
                                        "name": "Est. Cardinality",
                                        "value": "(unknown)"
                                    }
                                ],
                                "costs": [
                                    {
                                        "name": "Network",
                                        "value": "0.0"
                                    },
                                    {
                                        "name": "Disk I/O",
                                        "value": "0.0"
                                    },
                                    {
                                        "name": "CPU",
                                        "value": "0.0"
                                    },
                                    {
                                        "name": "Cumulative Network",
                                        "value": "(unknown)"
                                    },
                                    {
                                        "name": "Cumulative Disk I/O",
                                        "value": "(unknown)"
                                    },
                                    {
                                        "name": "Cumulative CPU",
                                        "value": "(unknown)"
                                    }
                                ],
                                "compiler_hints": [
                                    {
                                        "name": "Output Size (bytes)",
                                        "value": "(none)"
                                    },
                                    {
                                        "name": "Output Cardinality",
                                        "value": "(none)"
                                    },
                                    {
                                        "name": "Avg. Output Record Size (bytes)",
                                        "value": "(none)"
                                    },
                                    {
                                        "name": "Filter Factor",
                                        "value": "(none)"
                                    }
                                ]
                            }
                        }
                    ]
                },
                "restartStrategy": "default",
                "startTime": 1471024932938,
                "state": "FINISHED",
                "statusCounts": {
                    "CREATED": 0,
                    "SCHEDULED": 0,
                    "DEPLOYING": 0,
                    "RUNNING": 0,
                    "FINISHED": 3,
                    "CANCELING": 0,
                    "CANCELED": 0,
                    "FAILED": 0
                },
                "timestamps": {
                    "CREATED": 1471024932938,
                    "RUNNING": 1471024933051,
                    "FAILING": 0,
                    "FAILED": 0,
                    "CANCELLING": 0,
                    "CANCELED": 0,
                    "FINISHED": 1471024935012,
                    "RESTARTING": 0,
                    "SUSPENDED": 0
                },
                "vertices": [
                    {
                        "id": "acdb73c5f4dd53d60b11c814b1512900",
                        "name": "CHAIN DataSource (at main(WordCount.java:52) (org.apache.flink.api.java.io.CollectionInputFormat)) -> FlatMap (FlatMap at main(WordCount.java:61)) -> Combine(SUM(1), at main(WordCount.java:64)",
                        "parallelism": 1,
                        "status": "FINISHED",
                        "duration": 1488,
                        "tasks": {
                            "CREATED": 0,
                            "SCHEDULED": 0,
                            "DEPLOYING": 0,
                            "RUNNING": 0,
                            "FINISHED": 1,
                            "CANCELING": 0,
                            "CANCELED": 0,
                            "FAILED": 0
                        },
                        "metrics": {
                            "read-bytes": 0,
                            "write-bytes": 245,
                            "read-records": 0,
                            "write-records": 26
                        },
                        "startTime": 1471024933092,
                        "endTime": 1471024934580
                    },
                    {
                        "id": "65c08171f8232e197e0cca1da43c4968",
                        "name": "Reduce (SUM(1), at main(WordCount.java:64)",
                        "parallelism": 1,
                        "status": "FINISHED",
                        "duration": 316,
                        "tasks": {
                            "CREATED": 0,
                            "SCHEDULED": 0,
                            "DEPLOYING": 0,
                            "RUNNING": 0,
                            "FINISHED": 1,
                            "CANCELING": 0,
                            "CANCELED": 0,
                            "FAILED": 0
                        },
                        "metrics": {
                            "read-bytes": 245,
                            "write-bytes": 245,
                            "read-records": 26,
                            "write-records": 26
                        },
                        "startTime": 1471024934571,
                        "endTime": 1471024934887
                    },
                    {
                        "id": "656fc1e6b1806fc45f7453fd4323e6bf",
                        "name": "DataSink (collect())",
                        "parallelism": 1,
                        "status": "FINISHED",
                        "duration": 134,
                        "tasks": {
                            "CREATED": 0,
                            "SCHEDULED": 0,
                            "DEPLOYING": 0,
                            "RUNNING": 0,
                            "FINISHED": 1,
                            "CANCELING": 0,
                            "CANCELED": 0,
                            "FAILED": 0
                        },
                        "metrics": {
                            "read-bytes": 245,
                            "write-bytes": 0,
                            "read-records": 26,
                            "write-records": 0
                        },
                        "startTime": 1471024934875,
                        "endTime": 1471024935009
                    }
                ]
            }
        ]
    }
}

```