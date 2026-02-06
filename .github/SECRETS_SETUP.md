# Konfigurasi GitHub Secrets untuk Maven Deployment

Workflow otomatis untuk deployment ke Maven repository memerlukan konfigurasi secrets di GitHub. Ikuti langkah-langkah berikut untuk mengatur secrets yang diperlukan.

## Secrets yang Diperlukan

Workflow ini memerlukan 3 secrets untuk autentikasi dan konfigurasi Maven repository:

1. **MAVEN_USERNAME** - Username untuk autentikasi ke Maven repository
2. **MAVEN_PASSWORD** - Password atau token untuk autentikasi ke Maven repository
3. **MAVEN_REPO_URL** - URL dari Maven repository target (contoh: `https://repo.minekarta.com/snapshots`)

## Cara Mengatur Secrets di GitHub

### Langkah 1: Buka Repository Settings
1. Buka repository `RendangMC/StegripePlugin` di GitHub
2. Klik tab **Settings** di bagian atas repository

### Langkah 2: Navigasi ke Secrets and Variables
1. Di sidebar kiri, klik **Secrets and variables**
2. Pilih **Actions**

### Langkah 3: Tambahkan Secrets
1. Klik tombol **New repository secret**
2. Tambahkan secret pertama:
   - **Name**: `MAVEN_USERNAME`
   - **Value**: Masukkan username untuk Maven repository
   - Klik **Add secret**

3. Klik tombol **New repository secret** lagi
4. Tambahkan secret kedua:
   - **Name**: `MAVEN_PASSWORD`
   - **Value**: Masukkan password atau token untuk Maven repository
   - Klik **Add secret**

5. Klik tombol **New repository secret** lagi
6. Tambahkan secret ketiga:
   - **Name**: `MAVEN_REPO_URL`
   - **Value**: Masukkan URL Maven repository target (contoh: `https://repo.minekarta.com/snapshots`)
   - Klik **Add secret**

## Cara Kerja Workflow

Setelah secrets dikonfigurasi, workflow akan:

1. **Trigger**: Otomatis berjalan setiap kali ada push ke branch `master` atau `dev`
2. **Build**: Mengompilasi project menggunakan Maven dengan Java 21
3. **Deploy**: Mengunggah artifact ke Maven repository yang dikonfigurasi di `pom.xml` dan environment variable `MAVEN_REPO_URL`

## Testing Workflow

Setelah secrets dikonfigurasi dan workflow dibuat:

1. Push commit ke branch `master` atau `dev`
2. Buka tab **Actions** di repository GitHub
3. Anda akan melihat workflow "Publish to Maven Repository" berjalan
4. Klik pada workflow run untuk melihat detail dan log

## Troubleshooting

### Workflow Gagal dengan Error Authentication
- Pastikan `MAVEN_USERNAME`, `MAVEN_PASSWORD`, dan `MAVEN_REPO_URL` sudah dikonfigurasi dengan benar
- Pastikan credentials yang digunakan memiliki permission untuk deploy ke repository
- Pastikan `MAVEN_REPO_URL` adalah URL yang valid dan dapat diakses

### Build Gagal
- Periksa log di tab Actions untuk melihat error spesifik
- Pastikan code di branch master dapat di-build dengan `mvn clean package`

### Artifact Tidak Muncul di Repository
- Pastikan `distributionManagement` di `pom.xml` sudah dikonfigurasi dengan benar
- Pastikan repository URL dapat diakses dan credentials valid

## Informasi Tambahan

### Maven Repository Configuration
Repository target dikonfigurasi di `pom.xml` dan mengambil nilai dari GitHub secret:
```xml
<distributionManagement>
    <snapshotRepository>
        <id>rendangmc-repo</id>
        <url>${env.MAVEN_REPO_URL}</url>
    </snapshotRepository>
</distributionManagement>
```

Di `.github/workflows/maven-publish.yml`:
```yaml
env:
  MAVEN_REPO_URL: ${{ secrets.MAVEN_REPO_URL }}
```

Untuk mengubah repository target, cukup ubah nilai secret `MAVEN_REPO_URL` di GitHub repository settings.

### Version Management
- Project saat ini menggunakan version `1.0-SNAPSHOT`
- Untuk release version (bukan snapshot), tambahkan konfigurasi repository di `distributionManagement`
- Artifact yang di-deploy:
  - `plugin-parent:1.0-SNAPSHOT` (parent POM)
  - `plugin-core:1.0-SNAPSHOT` (core library)
  - `plugin-example:1.0-SNAPSHOT` (example plugin)

## Security Best Practices

1. **Jangan pernah** commit credentials langsung ke code
2. Gunakan secrets untuk semua informasi sensitif
3. Rotasi password secara berkala
4. Gunakan token dengan permission minimal yang diperlukan
5. Monitor workflow runs untuk aktivitas yang mencurigakan
