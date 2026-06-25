# Black Box Testing

## Build
- [ ] Run `.\gradlew.bat :app:assembleDebug`.
- [ ] Install APK on emulator/device.
- [ ] Open app without crash.

## Flow Awal
- [ ] Splash appears.
- [ ] Onboarding can continue and skip.
- [ ] Login screen opens.
- [ ] Google login button opens next flow placeholder.
- [ ] Setup motor saves name, plate number, and current kilometer.
- [ ] Required fields show input errors near fields.

## Motor
- [ ] Add motor.
- [ ] Show motor list.
- [ ] Open motor detail.
- [ ] Edit motor.
- [ ] Set active motor.
- [ ] Delete motor with confirmation.
- [ ] Empty state appears when no motor exists.

## Dashboard
- [ ] Active motor appears.
- [ ] Feature shortcuts show Motor, Servis, Oli, Pajak.
- [ ] Monthly expense card appears.
- [ ] Service/oil/fuel/tax values update from local records.
- [ ] Bottom nav opens Home, Motor, Catat, Riwayat, Profil.

## Servis
- [ ] Add servis.
- [ ] Required fields validate.
- [ ] Servis appears in history.
- [ ] Detail opens.
- [ ] Edit servis.
- [ ] Delete servis with confirmation.
- [ ] Next service target uses current KM, last service KM, and interval.

## Oli
- [ ] Add oli.
- [ ] Required fields validate.
- [ ] Oli appears in history.
- [ ] Detail opens.
- [ ] Edit oli.
- [ ] Delete oli with confirmation.
- [ ] Next oil target uses last oil KM and interval.

## Bensin
- [ ] Open fuel form from Catat.
- [ ] Fetch realtime fuel price.
- [ ] Manual price fallback works when API fails.
- [ ] Add fuel record.
- [ ] Fuel appears in history.
- [ ] Detail opens.
- [ ] Edit fuel.
- [ ] Delete fuel with confirmation.

## Pajak
- [ ] Add pajak.
- [ ] Required fields validate.
- [ ] Pajak appears in history.
- [ ] Detail opens.
- [ ] Edit pajak.
- [ ] Delete pajak with confirmation.

## Profil
- [ ] Profile opens from bottom nav.
- [ ] Edit profile saves name and email.
- [ ] Settings opens.
- [ ] Backup page opens.
- [ ] Export/import actions show expected state.
- [ ] About page opens.

## Security And Data
- [ ] No API key is committed.
- [ ] `google-services.json` is not committed unless intentionally approved.
- [ ] Vehicle data stays local.
- [ ] No Firebase/Auth/cloud sync is required for MVP.
