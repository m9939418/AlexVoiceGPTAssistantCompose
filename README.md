# 🎙️ Alex Voice GPT Assistant (Compose + OpenAI Chat API)

一個使用 **Kotlin + Jetpack Compose + Hilt + OpenAI Chat API (gpt-4o-mini-audio-preview)** 實作的「語音助理 Demo App」。

功能包含：

- 使用 **SpeechRecognizer** 進行語音辨識（STT, Speech-to-Text）
- 呼叫 OpenAI Chat API，取得 **文字 + 語音（mp3, base64）** 回應
- 將回傳的 audio base64 轉成 `ByteArray`，使用 `MediaPlayer` 播放（TTS, Text-to-Speech）
- 使用 Jetpack Compose 建立簡單對話 UI（聊天泡泡 + Mic FAB）


---

## 📸 Demo

---
<video controls width="360">
  <source src="https://private-user-images.githubusercontent.com/10461692/523450667-4909c1be-d4fd-4879-b8e0-51c40f53868a.mp4?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NjUxMDU5NDUsIm5iZiI6MTc2NTEwNTY0NSwicGF0aCI6Ii8xMDQ2MTY5Mi81MjM0NTA2NjctNDkwOWMxYmUtZDRmZC00ODc5LWI4ZTAtNTFjNDBmNTM4NjhhLm1wND9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTEyMDclMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUxMjA3VDExMDcyNVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTRhNWVhYjI4ZTE5NGUyYjUwZmMyMWU4MjcxYmFhYWYzZmRlZThmZDNlNjFmZGU3OTI1NGMxZWNiMmQzNDEwODgmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.elwpJGxjcBam_0-7g2Hx9v-u-qucZBv1lYnl1QJ7OjY" type="video/mp4">
  Your browser does not support the video tag.
</video>

[📥 下載 Demo 影片](https://private-user-images.githubusercontent.com/10461692/523450667-4909c1be-d4fd-4879-b8e0-51c40f53868a.mp4?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NjUxMDU5NDUsIm5iZiI6MTc2NTEwNTY0NSwicGF0aCI6Ii8xMDQ2MTY5Mi81MjM0NTA2NjctNDkwOWMxYmUtZDRmZC00ODc5LWI4ZTAtNTFjNDBmNTM4NjhhLm1wND9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTEyMDclMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUxMjA3VDExMDcyNVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTRhNWVhYjI4ZTE5NGUyYjUwZmMyMWU4MjcxYmFhYWYzZmRlZThmZDNlNjFmZGU3OTI1NGMxZWNiMmQzNDEwODgmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.elwpJGxjcBam_0-7g2Hx9v-u-qucZBv1lYnl1QJ7OjY)

---

## 📱 功能與流程概述

1. 使用者點擊畫面下方的 **Mic 按鈕**。
2. App 檢查並請求 `RECORD_AUDIO` 權限。
3. 權限允許後，呼叫 `Speech2TextManager.startListening()` 開始錄音。
4. 系統透過 `SpeechRecognizer` 回傳 STT 事件（Ready / Final / Error 等）。
5. `VoiceViewModel` 監聽這些事件，當收到 `Final(text)` 時：
    - 在畫面上新增一則 User 聊天泡泡。:contentReference[oaicite:7]{index=7}
    - 將 user 訊息加入 `messagesHistory`，並呼叫 OpenAI API。
6. OpenAI 回應後，mapper 將 `ChatDto` 轉成 Domain `Chat`：
    - 解析 `audio.transcript` → 顯示在 UI。
    - 將 `audio.data` base64 decode 成 `audioBytes` → 交給 `AudioPlayer` 播放。
7. 在畫面上新增 Assistant 聊天泡泡，並播放語音回覆。

---

## 🏗 專案架構

### Package 結構

```text
com.alex.yang.alexvoicegptassistantcompose
├─ MainActivity.kt
├─ core
│  ├─ di
│  │  └─ ApiModule.kt
│  ├─ audio
│  │  └─ AudioPlayer.kt
│  ├─ stt
│  │  ├─ Speech2TextManager.kt
│  │  └─ Speech2TextEvent.kt
│  └─ utils
│     └─ network
│        ├─ OpenAIAuthInterceptor.kt
│        └─ Resource.kt
└─ feature.voice
   ├─ data
   │  ├─ api
   │  │  └─ OpenAIApi.kt
   │  ├─ mapper
   │  │  └─ ChatMapper.kt
   │  ├─ model
   │  │  ├─ AudioRequest.kt
   │  │  ├─ ChatRequest.kt
   │  │  ├─ ChatDto.kt
   │  │  ├─ MessageRequest.kt
   │  │  ├─ MessageDto.kt
   │  │  ├─ AudioDto.kt
   │  │  └─ ChoiceDto.kt
   │  └─ repository
   │     └─ VoiceRepositoryImpl.kt
   ├─ domain
   │  ├─ model
   │  │  └─ Chat.kt
   │  ├─ repository
   │  │  └─ VoiceRepository.kt
   │  └─ usecase
   │     └─ FetchChatMessagesUseCase.kt
   └─ presentation
      ├─ VoiceViewModel.kt
      ├─ VoiceScreen.kt
      ├─ ChatMessage.kt
      ├─ component
      │  └─ ChatBubble.kt
      └─ di
         ├─ VoiceBinds.kt
         └─ VoiceModule.kt
````

---

## ⚙️ 技術棧

* **語言與架構**

    * Kotlin
    * Jetpack Compose（UI）
    * MVVM + UseCase + Repository（Clean-ish 分層）
* **DI**

    * Hilt（`@HiltViewModel`, `@Module`, `@Binds`, `@InstallIn`）
* **網路**

    * Retrofit + OkHttp + HttpLoggingInterceptor
    * Kotlinx Serialization Converter（`.asConverterFactory("application/json")`）
    * 自訂 `OpenAIAuthInterceptor` 自動帶入 `Authorization: Bearer <API_KEY>`
* **非同步**

    * Kotlin Coroutines + Flow / SharedFlow + `collectLatest`
* **STT / TTS**

    * Android `SpeechRecognizer` + `RecognizerIntent`（STT）
    * `MediaPlayer` 播放臨時 mp3 檔案（TTS）
* **OpenAI**

    * Chat API: `POST /v1/chat/completions`
    * 模型：`gpt-4o-mini-audio-preview`（文字 + 語音）

---

## 🚀 專案執行方式

> 以下步驟假設你使用 Android Studio（Giraffe 以後版本）與標準 Android 專案結構。

### 1. 取得 OpenAI API Key

到 OpenAI 平台建立 API Key，並於本機設定，例如：

* 在 `local.properties` 或 `gradle.properties` 中設定：

  ```properties
  OPENAI_API_KEY=sk-xxxxxx
  ```

* 在 `BuildConfig` 或 `ApiModule` 中使用，如下：

  ```kotlin
  fun provideOpenAIAuthInterceptor() =
      OpenAIAuthInterceptor(BuildConfig.OPENAI_API_KEY)
  ```

> **請確認** `BuildConfig.OPENAI_API_KEY` 已正確透過 Gradle 填入。

---

### 2. 匯入專案

1. 開啟 Android Studio
2. `File > Open...` 選擇此專案資料夾
3. 等待 Gradle Sync 完成

---

### 3. 執行 App

1. 接上一支真實 Android 手機（建議 Android 10+）
2. 點選 Run ▶
3. **首次使用 Mic 功能會要求 RECORD_AUDIO 權限**，請選擇 Allow。

---

## 🧪 未來可改善項目（技術債）

這個 Demo 已經可以正常跑完 STT → Chat → TTS 流程，但仍有一些可以優化的地方：

1. **OpenAI ChatRequest 結構尚未完全對齊最新「multi-modal content parts」設計**

    * 目前 `MessageRequest` 是 `role + content(String)`，可改為 content = List<ContentPart> 以支援更豐富型別（input_text / input_audio 等）。

2. **錯誤顯示與重試機制較簡單**

    * `Resource.Error` 只顯示 message，UI 尚未提供 Retry 按鈕或自動重試。

3. **對話紀錄只存在記憶體中**

    * `messagesHistory` 與 `UiState.messages` 都在記憶體內維護，離開 Activity 會遺失，可改為搭配 Room / DataStore。

4. **沒有針對 STT / TTS 的狀態顯示**

    * 目前僅透過 `isListening` 控制 Mic 按鈕文案，未顯示錄音中視覺效果（例如波形 / 動畫）。

5. **尚未處理螢幕旋轉 / 多視窗狀態保存**

    * `messageId` 與 `messages` 沒有使用 `SavedStateHandle` 或其他保存方式，在配置變更時可能遺失。

---

## ✅ 結語

這個專案是一個非常乾淨的 **「語音 ChatGPT 助理」範例**：

* 已具備 **完整的端到端流程**：
  **STT → OpenAI Chat (文字+語音) → UI Chat Bubble → TTS 播放**

---
## 👤 Author

**Alex Yang**  
Android Engineer
🌐 [github.com/m9939418](https://github.com/m9939418)

