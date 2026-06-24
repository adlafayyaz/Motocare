# API And Realtime Plan

## Fuel Price API
- Repo: `https://github.com/alifmaulidanar/hargaBensin`
- Purpose: realtime BBM price for Jabodetabek.
- Source used by repo: `https://isibens.in/`
- Demo base URL: `https://api.alifmaulidanar.my.id/api-bbm/`

## Endpoints
- All BBM:
  - `/api-bbm/full`
- Specific BBM:
  - `/api-bbm/{jenis}/{merek}/{oktan}`

## Parameters
- `jenis`: `bensin` or `diesel`
- `merek`: `Pertamina`, `Vivo`, `BP`, `Shell`
- `oktan` for bensin: `90`, `92`, `95`, `98`
- `oktan` for diesel: `48`, `51`, `53`

## MotoCare Usage
- Use API only to fetch current fuel price.
- Store user fuel record locally.
- Store fuel price snapshot in local database when user saves a record.
- If API fails, allow manual price input.
- Cache latest API result locally with `update` date.

## Fuel Form Fields
- motor_id
- tanggal
- jenis_bbm
- merek_bbm
- oktan
- harga_per_liter
- liter
- total_biaya
- kilometer

## Realtime Recommendation
- Bensin price: yes, use API.
- Pajak: not recommended for MVP.
- Servis: not from external API.
- Oli: not from external API.

## Pajak Realtime
- Possible only if there is official regional Samsat API.
- Risk:
  - different API per province
  - often needs plat, NIK, captcha, or web scraping
  - privacy risk
  - unstable endpoint
- MVP decision:
  - manual due date
  - manual estimated cost
  - in-app status only

## Servis Realtime
- No universal realtime service API.
- Better computed locally:
  - current kilometer
  - last service kilometer
  - service interval
  - target kilometer
  - remaining kilometer
- Optional future:
  - bengkel catalog
  - service price estimate by type
  - manual maintenance template per motor

## Oli Realtime
- No external realtime needed.
- Better computed locally:
  - current kilometer
  - last oil change kilometer
  - oil interval
  - target kilometer
  - remaining kilometer

## Local Live Updates
- Dashboard updates immediately after CRUD.
- Service progress updates after kilometer update.
- Oil progress updates after kilometer update.
- Tax days remaining updates by date.
- Monthly expense updates after records change.

## Network Dependency
- Fuel price API is optional.
- App must still work offline.
- API failure must not block save.
