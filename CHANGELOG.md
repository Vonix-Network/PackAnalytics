# Changelog

All notable changes to this project will be documented in this file.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [1.0.4-vonix.1] - 2026-06-25

Production-hardening fork by [Vonix-Network](https://github.com/Vonix-Network).
Upstream: [txnimc/PackAnalytics](https://github.com/txnimc/PackAnalytics).

### Fixed
- `HttpURLConnection` now has a 5s (configurable) connect AND read timeout. Previously, an unreachable endpoint would hang the keepalive thread on the socket for the JVM default (30s+) every cycle.
- Keepalive exception path no longer prints a full stack trace at `ERROR`; it logs a single `WARN` line with the message. Eliminates the 24h/server log-spam observed on Vonix.Network's Otherworld server.
- Non-200 response codes are demoted from `ERROR + stacktrace` to a single `WARN` line.
- Circuit breaker added: after N consecutive failures (default 5) the polling task suspends and only probes every Nth normal cycle (12 = ~1h with the default 5-minute updateRate), instead of hammering a dead endpoint forever.

### Added
- `enableKeepalive` (default `true`) — master switch; when `false`, `startKeepAliveTask()` is a no-op and no scheduler is ever started.
- `keepaliveTimeoutSeconds` (default `5`) — connect/read timeout for each keepalive HTTP request.
- `circuitBreakerThreshold` (default `5`) — consecutive failure count before the circuit breaker engages.

### Changed
- Demoted keepalive `LOGGER.error(..., e)` calls to `LOGGER.warn("...: {}", e.getMessage())` (no stacktrace argument).
- `mod.version` bumped `1.0.3` → `1.0.4-vonix.1`.
- `mod.author` is now `Toni, Vonix-Network`; added `mod.fork_of=txnimc/PackAnalytics`.
- `mods.toml` `displayURL` / `issueTrackerURL` now point to the Vonix-Network fork.
