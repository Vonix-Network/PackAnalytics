# PackAnalytics — Vonix-Network Fork

[![Build](https://github.com/Vonix-Network/PackAnalytics/actions/workflows/build.yml/badge.svg)](https://github.com/Vonix-Network/PackAnalytics/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE.md)
[![Upstream](https://img.shields.io/badge/upstream-txnimc%2FPackAnalytics-blue)](https://github.com/txnimc/PackAnalytics)

Production-hardened fork of [txnimc/PackAnalytics](https://github.com/txnimc/PackAnalytics) maintained by
[Vonix-Network](https://github.com/Vonix-Network) for use on the Vonix.Network Otherworld Minecraft server
and any modpack author who wants a keepalive that won't flood logs when the metrics endpoint is down.

All credit for the original mod goes to **Toni (txnimc)** — go check out the upstream project.

---

## Why we forked

After running PackAnalytics on the Vonix.Network Otherworld 1.20.1-Forge server, we hit three
operational problems that made the mod painful to ship to players:

1. **No HTTP timeout.** `HttpURLConnection` was constructed without `setConnectTimeout` /
   `setReadTimeout`, so when the metrics endpoint went down the keepalive thread blocked on
   the socket for the JVM default (~30 s) every cycle.
2. **Full stacktrace at ERROR every failure.** The catch block logged
   `LOGGER.error("...: " + e.getMessage(), e)`, dumping a multi-line stacktrace at ERROR every
   `updateRate` minutes (5 by default). 24 h of an unreachable endpoint produced hundreds of
   ERROR-level stacktraces in crash reports and log uploads.
3. **No circuit breaker.** The scheduler kept re-banging a known-dead endpoint at full
   cadence forever.

This fork fixes all three and adds a master `enableKeepalive` kill-switch for operators who
want the analytics off entirely.

A patch covering points 1–3 has been [proposed upstream](https://github.com/txnimc/PackAnalytics/pulls)
as well — this fork exists so we can ship the fix on our servers immediately and so other
modpack maintainers can pull a pre-built jar without waiting on the upstream merge.

## What's new in `1.0.4-vonix.1`

See [CHANGELOG.md](CHANGELOG.md). Highlights:

- Connect + read timeouts on the keepalive HTTP request.
- Single WARN line on failure, no stacktrace.
- Exponential-backoff circuit breaker (5 fails → 1-hour probe interval).
- `enableKeepalive`, `keepaliveTimeoutSeconds`, `circuitBreakerThreshold` config flags.

## Configuration

The mod config lives at `config/packanalytics-common.toml` after first launch. New flags:

| Key                        | Default | Description |
| -------------------------- | ------- | ----------- |
| `Enable Keepalive`         | `true`  | Master switch. When `false`, no scheduler is started and no requests are sent. |
| `Keepalive Timeout (s)`    | `5`     | Connect + read timeout for each HTTP request. |
| `Circuit Breaker Threshold`| `5`     | Consecutive failure count before keepalive suspends to ~1-hour probe cycles. |

Existing flags (`Metrics Endpoint URL`, `Pack ID`, `Update Rate`) keep their upstream semantics.

## Supported versions

This fork ships pre-built jars for **Minecraft 1.20.1 — Forge** (the version Vonix.Network
runs in production). The Stonecutter multi-version build is preserved upstream, so other
loaders/versions can still be built locally with `./gradlew "<version>:build"`.

## Building locally

```bash
JAVA_HOME=/path/to/jdk-21 ./gradlew "1.20.1-forge:build" --no-daemon
```

JDK 21 is required for the Stonecutter/Loom toolchain (bytecode targets MC 1.20.1's Java 17,
but the build tooling itself needs JVM 21).

## Credits & License

- Original mod © **Toni (txnimc)** — <https://github.com/txnimc/PackAnalytics>
- Fork maintenance © **Vonix-Network** — <https://github.com/Vonix-Network>
- Licensed under the [MIT License](LICENSE.md).

## Reporting issues

For bugs in the Vonix fork: <https://github.com/Vonix-Network/PackAnalytics/issues>.
For upstream bugs / feature requests, please file with txnimc.
