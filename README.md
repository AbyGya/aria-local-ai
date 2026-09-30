# Aria — Local AI Assistant for Android

Aria is a privacy-first, on-device AI assistant for Android. It can think, learn, and execute commands — all running locally on your phone.

## Features

- **On-Device LLM**: Powered by llama.cpp with Qwen 3.5 / Gemma 3 models
- **Agentic Engine**: Multi-step planning, tool calling, and execution
- **Phone Automation**: SMS, calls, notifications, files, settings
- **Smart Home**: MQTT and Home Assistant integration
- **Memory & RAG**: Long-term memory with vector search
- **LocalAI Bridge**: Connect to a local server for heavy tasks
- **Bilingual**: English and Bahasa Indonesia
- **Minimalist Organic Theme**: Warm, natural design

## Architecture

```
Aria (Android App)
├── On-Device (Primary)
│   ├── Qwen 3.5 4B (llama.cpp JNI)
│   ├── Agentic Engine
│   ├── Memory + RAG
│   └── Phone Automation
├── LocalAI Server (Optional Bridge)
│   ├── Heavy models (70B+, vision, TTS)
│   └── Multi-modal tasks
└── Cloud (Optional)
    └── API key (user-provided)
```

## Building

### Prerequisites

- Android Studio Hedgehog or newer
- JDK 17
- Android SDK 34+
- NDK 27+

### Build Commands

```bash
# Debug build
./gradlew assembleFossDebug

# Release build
./gradlew assembleFossRelease

# Run tests
./gradlew test
```

## Models

| Model | Size | RAM | Best For |
|-------|------|-----|----------|
| Qwen 3.5 0.8B | 530 MB | 4 GB | Quick tasks |
| Qwen 3.5 4B | 2.5 GB | 8 GB | Best balance |
| Gemma 3 1B | 800 MB | 4 GB | Low RAM |

## License

MIT
