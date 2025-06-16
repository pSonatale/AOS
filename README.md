# 🎵 Sonatale - 감정 기반 유아 동화 BGM 생성 앱
졸업작품 2분반 8조 레포지토리입니다.

**Sonatale**는 유아 동화 낭독 시 실시간 감정 분석을 통해, 어린이의 감정 몰입을 돕는 감정 기반 배경음악(BGM)을 자동 생성하는 AI 애플리케이션입니다. 교사의 수업 준비 부담을 줄이고, 아이들의 정서 발달과 창의력을 동시에 촉진합니다.

<p align="center">
  <img src="https://github.com/user-attachments/assets/fc1621e6-50f7-45f7-b70a-0be928134aa2" width="200"/>
  <img src="https://github.com/user-attachments/assets/e686ae95-020d-4a0d-a68f-f8adf0394920" width="200"/>
  <img src="https://github.com/user-attachments/assets/192b1f12-46d0-48c7-8fa6-72cb7b1cb6e8" width="200"/>
</p>

## 🌟 주요 기능

- 🎙 실시간 음성 → 텍스트(STT) 변환 (Google Speech-to-Text API 사용)
- 🧠 다중 감정 분석 (BERT 기반 43개 감정 분류 모델)
- 🎼 감정 기반 음악 생성 (Meta MusicGen 기반)
- 💾 동화 + BGM 저장 및 재생 기능
- 📱 직관적인 모바일 UI/UX (유아 대상 최적화)

## 📄 API 명세

Sonatale 프로젝트의 주요 API 명세는 아래와 같습니다.

### 🎵 P-1: 감정 기반 음원 재생
- **Method**: `GET`  
- **URL**: `/tts/play`  
- **설명**: 텍스트 입력에 대응하는 감정 기반 `.m4a` 음원을 스트리밍으로 반환
- **요청 예시**: GET /tts/play?text=너무 우울해
  
| 필드명 | 타입   | 필수 | 설명                          |
|--------|--------|------|-------------------------------|
| text   | String | ✅    | 감정 분석용 입력 텍스트 (키) |

-**응답**: Content-Type: `audio/m4a` / Body: 감정에 매핑된 음원 스트리밍

### 📚 P-4: 저장된 텍스트-음원 매핑 목록 조회
- **Method**: `GET`  
- **URL**: `/tts/list`  
- **설명**: 저장된 키-음원 매핑 목록을 반환
- **요청 예시**: GET /tts/list

| 필드명 | 타입   | 필수 | 설명                          |
|--------|--------|------|-------------------------------|
| key   | String | ✅    | 저장될 음원 이름과 매핑됨 |
| value   | String | ✅    | 확장자 .m4a 권장 |

-**응답 예시**: { "hi": "static/a1.m4a" }

### 🎼 P-5: 텍스트 기반 AI 음악 생성 (MusicGen)
- **Method**: `POST`  
- **URL**: `/api/music`  
- **설명**: 사용자 입력 프롬프트와 길이를 기반으로 MusicGen 모델로 음악 생성
- **요청 예시**: POST /api/music { "promt": "불안", "duration": 10 }

| 필드명 | 타입   | 필수 | 설명                          |
|--------|--------|------|-------------------------------|
| prompt   | String | ✅    | 음악 생성 프롬프트 |
| duration   | int | ✅    | 생성할 음악 길이 (초) |

-**응답 예시**: { "filePath": "/music/output_1718200112.wav", "duration": 10, "prompt": "불안"}

### 💬 P-6: 텍스트 감정 분석
- **Method**: `GET`  
- **URL**: `/api/emotion`  
- **설명**: 입력 텍스트의 감정을 분석하여 라벨 반환
- **요청 예시**: GET /api/emotion?text=나는 너무 피곤해

| 필드명 | 타입   | 필수 | 설명                          |
|--------|--------|------|-------------------------------|
| text   | String | ✅    | 분석할 텍스트 |

-**응답 예시**: { "피곤함" }



<h2>
  Contributors
</h2>

| 강민재 | 김서영 | 김헤정 | 홍강래 |
|:---------------------------:|:-------------------------:|:------------------------------:|:------------------------------:|
| 백엔드 | 백엔드 | 프론트엔드 | AI |
