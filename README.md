[![Translation status](https://translate.codeberg.org/widget/overload/android/svg-badge.svg)](https://translate.codeberg.org/engage/overload/)

# Overload

Overload is a user-friendly native app designed to facilitate time tracking for everyone.


## Contributing

Contributions are always welcome!

Just create issues and pull requests or help [translating](https://translate.codeberg.org/engage/overload/) on Weblate :)


## Features

- create time spans
- automagically creates pauses in between
- delete time spans - on-by-one or all-together
- scroll through days with ease
- backup your data as .csv
- import backups
- set goals


## Feedback

Feedback and suggestions are more than welcome! Please reach out by creating an issue :)


## Installation

<a href="https://f-droid.org/app/cloud.pablos.overload"><img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png" width="170"></a>

Alternatively go to the releases tab on Codeberg and download an apk.


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


### What are ongoing pauses?
By showing the duration between the last item and the current time you can determine how long you stopped or paused working since then. You can plan how much longer your pause is at any given moment as the duration updates in real time.

### Why can't I delete an ongoing pause? {#delete-pause}
You cannot delete ongoing pauses as they are only there to indicate a pause will be created once you hit start again. If you will not hit start until the next day it will be gone and will not count against your goal. By deleting items that occurred beforehand, you can hide the pause.
Imagine you're using the app to track your work hours. You take a break, and an ongoing pause is created. If you get ill during the day or decide not to work on the day for any other reason, the ongoing pause will vanish without affecting your work-time goal.

### Why does the app annoy me with a popup to adjust the end? {#spread-across-days}
Sometimes we forget stuff. Sometimes we do stuff across days - like sleeping. Nevertheless when an item is still ongoing from yesterday or days before yesterday, you need to set an end or confirm you want to spread the item across multiple days. The popup will be gone once there are no more ongoing items from the past days ;)