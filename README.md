# Aria — Local AI Assistant for Android

![Version](https://img.shields.io/badge/version-0.11.0-orange.svg)

Aria is a privacy-first, on-device AI assistant for Android. It can plan, learn,
and act on the phone — inference runs locally, and nothing leaves the device
unless the user points Aria at a server themselves.

## Who it's for

People who want an assistant that keeps working on a plane, in a dead spot, or
with the SIM pulled, and who would rather their prompts never touched someone
else's server. The trade is honest: a model small enough to live on a phone is
weaker at long reasoning than a frontier one, and Aria covers that with tools,
memory and an optional bridge to a bigger model on hardware the user controls.

## Requirements

- Android 14 (API 34) or newer
- arm64-v8a device — 4 GB RAM for the small models, 8 GB for a 4B model
- About 3–5 GB of free storage for the model weights
- JDK 21 and the Android SDK (34+ platforms, build-tools) to build from source

## Install

Download the APK from the repository's
[Actions](https://github.com/AbyGya/aria-local-ai/actions) page
(**Build Aria APK** → *Artifacts* → `aria-release-apk`), then allow installs
from that source when your file manager asks. The app is also on the F-Droid
channel as the `foss` flavour, which carries no Google dependencies.

To build it yourself:

```bash
./gradlew assembleFossDebug     # debug APK
./gradlew assembleFossRelease   # release APK, debug-signed without a key
./gradlew check                # the full quality gate
```

## Features

- **On-device LLM** — llama.cpp over JNI, so any arm64 SoC is covered rather
  than only the parts with a vendor NPU SDK
- **Agentic engine** — multi-step planning, tool calling, and a
  human-in-the-loop gate before anything sensitive runs
- **Phone tools** — SMS, calls, notifications, files, alarms, apps, Wi-Fi
- **Smart home** — Home Assistant over its REST API
- **Memory and RAG** — on-device embeddings and vector retrieval
- **LocalAI bridge** — delegate a heavy task to a LocalAI server on your network
- **Bilingual** — English and Bahasa Indonesia
- **Minimalist Organic theme** — sage, terracotta, dusty blue, wheat

## Architecture

```
Aria (Android app)
├── On-device (primary)
│   ├── llama.cpp JNI engine
│   ├── agentic engine: plan → act → observe
│   ├── memory + RAG
│   └── phone & smart-home tools
├── LocalAI server (optional bridge)
│   └── heavier models the phone cannot host
└── Cloud providers (optional)
    └── only with a key the user supplies
```

## Documentation

- [User guide](docs/user-guide.md) — what each setting does
- [Cookbook](docs/cookbook.md) — per-node reference and recipes
- [FAQ](docs/faq.md)
- [External automation](docs/external-automation.md) — letting Tasker or an
  `adb` script trigger a pipeline
- [Architecture decisions](docs/decisions/README.md)

## Pre-release notice

This project is currently at **version 0.11.0** and is under active development
— not yet ready for everyday reliance.
One part in particular is a stub rather than a finished feature: the native
llama.cpp sources are not vendored yet, so no model can actually be loaded and
the on-device engine reports that instead of replying. The pipeline engine, tool
system, memory, RAG and MCP integration behind it are inherited from the
upstream project and do work. Treat a build as something to try, not something
to depend on.

## License

Apache License 2.0 — see [LICENSE](LICENSE).
