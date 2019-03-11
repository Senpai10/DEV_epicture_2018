# Epicture

The goal of this project is to use and implement online photo sharing API platforms. We must create a photo finder and browsing application the following platform: Imgur. The different aspects of mobile (or universal) applications development are to be taken into account during the creation of our project.

> Imgur is an image hosting and sharing site, favoured by users of social media and social news sites including Reddit, Twitter and Digg because of its ease of use and flexibility.
> It hosts images for free in various formats, including animated Gif files, supported by advertising, with paid-for professional accounts available.
> Those images are kept online for free, only deleted if the image is not accessed at least once during any six-month period.
> Users of the site can post comments, use tools to create new images and then share those images with both Imgur users and the wider internet.

# Language

  - Java

### Requirement

* [Android Studio] - Android Studio is the official integrated development environment (IDE) for Google's Android operating system, built on JetBrains' IntelliJ IDEA software and designed specifically for Android development.
* [Gradle] - Gradle is an open-source build automation system that builds upon the concepts of Apache Ant and Apache Maven and introduces a Groovy-based domain-specific language (DSL) instead of the XML form used by Apache Maven for declaring the project configuration

### Navigation Menu

| Tabs | Functionalities |
| ------ | ------ |
| Home | Display the feed |
| Search | Allow you to search image with some filters |
| Upload | Upload an image to imgur from your gallery |
| Favorite | Display the photo you like it |
| Profile | Display the photos put online by the user connected |

### API Request

| API Request | Functionalities |
| ------ | ------ |
| https://api.imgur.com/oauth2/authorize | Authenticate to the Imgur platform |
| https://api.imgur.com/3/gallery/hot/rising | GET the feed popular and hot |
| https://api.imgur.com/3/account/me/images | GET the photos put online by the user connected |
| https://api.imgur.com/3/gallery/search/0?q={{image's name}} | GET the photos linked to your search without filter |
| https://api.imgur.com/3/gallery/search/viral/0?q={{image's name}} | GET the photos linked to your search with the viral filter |
| https://api.imgur.com/3/gallery/search/top/0?q={{image's name}} | GET the photos linked to your search with the hot filter |
| https://api.imgur.com/3/image | POST an image to imgur from your gallery |
| https://api.imgur.com/3/image/{{image's id}}/favorite | POST a photo you like it |
| https://api.imgur.com/3/account/me/favorites | GET the photos you like them |

### Run

```sh
$ Import the project into Android Studio
$ Install gradle
$ Build the project & Run
```



**Authors**

Guilhem AMARDEILH
Charly DAI


   [Android Studio]: <https://developer.android.com/studio/>
   [Gradle]: <https://gradle.org/>
