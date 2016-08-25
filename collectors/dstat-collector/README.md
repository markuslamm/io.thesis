## dstat-collector

Collects data provided by dstat system utility and exports the result:
```json
{
    "clientTimestamp": "2016-08-12T20:59:30.608",
    "clientHost": "127.0.1.1",
    "clientPort": 9091,
    "instanceId": "collector-client:25ba074a0d81dd5c8e2c7b4c73152dd2",
    "collectorType": "dstat",
    "data": {
        "disk": {
            "read": 112.0,
            "write": 436.0,
            "utilization": 6.49,
            "transactions": {
                "read": 5.72,
                "write": 8.9
            }
        },
        "process": {
            "runnable": 0.0,
            "uninterruptible": 0.0,
            "new": 1.4,
            "count": 271,
            "latency-highest-total": {
                "name": "compiz",
                "value": 8475.0
            },
            "latency-highest-avg": {
                "name": "cat",
                "value": 101.0
            }
        },
        "memory": {
            "usage": {
                "used": 5548032.0,
                "buffer": 217088.0,
                "cache": 1626112.0,
                "free": 695296.0
            },
            "process-most-expensive": {
                "name": "java",
                "value": 1510400.0
            },
            "paging": {
                "in": 1.22265625,
                "out": 10.8
            },
            "swap": {
                "used": 312320.0,
                "free": 7987200.0
            },
            "vm": {
                "hardPageFaults": 0.5,
                "softPageFaults": 990.0,
                "allocated": 1476.0,
                "free": 1500.0
            }
        },
        "system": {
            "load-avg": {
                "1m": 1.16,
                "5m": 1.28,
                "15m": 1.17
            },
            "interrupts": 1225.0,
            "contextSwitches": 3178.0,
            "ipc": {
                "messageQueue": 0.0,
                "semaphores": 0.0,
                "sharedMemory": 29.0
            },
            "unix-sockets": {
                "datagram": 38,
                "stream": 799,
                "listen": 38,
                "active": 761
            }
        },
        "io": {
            "read": 5.72,
            "write": 8.9,
            "process-most-expensive": {
                "name": "upstart",
                "pid": 3210,
                "read": 502.0,
                "write": 210.0,
                "cpuPercentage": 0.0
            },
            "filelocks": {
                "posix": 33.0,
                "flock": 4.0,
                "read": 13.0,
                "write": 25.0
            },
            "filesystem": {
                "openFiles": 13344,
                "inodes": 48582.0
            }
        },
        "cpu": {
            "usage": [
                {
                    "name": "cpu0",
                    "user": 17.0,
                    "system": 3.3,
                    "idle": 77.0,
                    "wait": 2.6,
                    "hwInterrupt": 0.0,
                    "swInterrupt": 0.0
                },
                {
                    "name": "cpu1",
                    "user": 17.0,
                    "system": 3.4,
                    "idle": 77.0,
                    "wait": 2.7,
                    "hwInterrupt": 0.0,
                    "swInterrupt": 0.4
                }
            ],
            "process-most-expensive": {
                "name": "java",
                "pid": 32528,
                "cpuPercentage": 5.8,
                "read": 102.0,
                "write": 272.0
            },
            "process-cpu-time-highest-total": {
                "name": "Xorg",
                "time": 40.1
            },
            "process-cpu-time-highest-avg": {
                "name": "docker-compos",
                "time": 225.0
            }
        },
        "net": {
            "traffic": [
                {
                    "name": "docker0",
                    "send": 0.0,
                    "received": 0.0
                },
                {
                    "name": "wlp2s0",
                    "send": 0.0,
                    "received": 0.0
                }
            ],
            "sockets": {
                "total": 1.0,
                "tcp": 19.0,
                "udp": 8.0,
                "raw": 0.0,
                "ipFragments": 0.0
            },
            "tcp-sockets": {
                "listen": 20.0,
                "established": 43.0,
                "syn": 0.0,
                "timeWait": 5.0,
                "close": 0.0
            },
            "udp": {
                "listen": 17.0,
                "active": 0.0
            }
        }
    }
}
```