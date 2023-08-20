
# Overload

Overload is a user-friendly native app designed to facilitate time tracking for everyone.


## Contributing

Contributions are always welcome!

Just create issues and pull requests ;)


## Features

- create time spans
- automagically creates pauses in between
- delete time spans - on-by-one or all-together
- scroll through days with ease
- backup your data as .csv
- import backups


## Feedback

Feedback and suggestions are more than welcome! Please reach out by creating an issue :)


## Installation

<a href="https://f-droid.org/app/cloud.pablos.overload"><img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png" width="170"></a>

## Authors

- [@pabloscloud](https://pablos.cloud)


## Related

This project draws inspiration from the jetpack compose sample [Reply](https://github.com/android/compose-samples/tree/main/Reply), which is licensed under the Apache License, Version 2.0.


## Screenshots

<table>
  <tr>
    <td><img src="https://codeberg.org/pabloscloud/Overload/raw/branch/main/screenshots/1.png" alt="Screenshot 1"></td>
    <td><img src="https://codeberg.org/pabloscloud/Overload/raw/branch/main/screenshots/2.png" alt="Screenshot 2"></td>
    <td><img src="https://codeberg.org/pabloscloud/Overload/raw/branch/main/screenshots/3.png" alt="Screenshot 3"></td>
    <td><img src="https://codeberg.org/pabloscloud/Overload/raw/branch/main/screenshots/4.png" alt="Screenshot 4"></td>
  </tr>
  <tr>
    <td><img src="https://codeberg.org/pabloscloud/Overload/raw/branch/main/screenshots/5.png" alt="Screenshot 1"></td>
    <td><img src="https://codeberg.org/pabloscloud/Overload/raw/branch/main/screenshots/6.png" alt="Screenshot 2"></td>
    <td><img src="https://codeberg.org/pabloscloud/Overload/raw/branch/main/screenshots/7.png" alt="Screenshot 3"></td>
    <td><img src="https://codeberg.org/pabloscloud/Overload/raw/branch/main/screenshots/8.png" alt="Screenshot 4"></td>
  </tr>
  <tr>
    <td><img src="https://codeberg.org/pabloscloud/Overload/raw/branch/main/screenshots/9.png" alt="Screenshot 1"></td>
    <td><img src="https://codeberg.org/pabloscloud/Overload/raw/branch/main/screenshots/10.png" alt="Screenshot 2"></td>
    <td><img src="https://codeberg.org/pabloscloud/Overload/raw/branch/main/screenshots/11.png" alt="Screenshot 3"></td>
    <td><img src="https://codeberg.org/pabloscloud/Overload/raw/branch/main/screenshots/12.png" alt="Screenshot 4"></td>
  </tr>
</table>


## FAQ

### How do I import a backup? {#import-backup}
Open your files app, choose the backup file, and locate the share icon or text. Tapping this icon will bring up a menu displaying various apps for sharing. From the list, select Overload to initiate the import. Wait until the import is finished, indicated by a completion message.

### Why does Overload rely on the Systems Sharesheet to import a backup?
Overload utilises the Systems Sharesheet for importing backups instead of requesting broad access to all files on your device. This approach avoids the need to seek permissions that could undermine trust in the project. Moreover, Overload's reliance on the Sharesheet ensures that the app only gains access to the specific file it requires, eliminating the necessity for extensive permissions.