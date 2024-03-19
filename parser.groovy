import groovy.json.JsonSlurper
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

class JsonDownloader {
    
    static void downloadJsonFilesWithGreatestVersion(String initialUrl) {
        try {
            // Step 1: Call initial URL and get JSON response
            def initialJsonContent = new URL(initialUrl).text
            def initialJsonData = new JsonSlurper().parseText(initialJsonContent)
            
            // Step 2: Iterate through each URL in JSON array and find the greatest version
            def greatestVersionUrl = findGreatestVersionUrl(initialJsonData.urls)
            
            // Step 3: Print URL containing greatest version
            println "URL containing greatest version: $greatestVersionUrl"
            
            // Step 4: Make another call to download all JSON files in the URL with greatest version
            def greatestVersionJsonContent = new URL(greatestVersionUrl).text
            def greatestVersionJsonData = new JsonSlurper().parseText(greatestVersionJsonContent)
            
            // Step 5: Downloads all files and stores it locally
            downloadAndStoreJsonFiles(greatestVersionJsonData.urls)
            
            println "All JSON files with the greatest version downloaded and stored locally."
        } catch (Exception e) {
            println "Error: ${e.message}"
        }
    }
    
    static String findGreatestVersionUrl(List<String> urls) {
        def greatestVersion = -1
        def greatestVersionUrl = ""
        
        urls.each { url ->
            def parts = url.split('/')
            def version = parts[-1].toInteger()
            
            if (version > greatestVersion) {
                greatestVersion = version
                greatestVersionUrl = url
            }
        }
        
        return greatestVersionUrl
    }
    
    static void downloadAndStoreJsonFiles(List<String> urls) {
        urls.each { url ->
            try {
                def jsonContent = new URL(url).text
                def fileName = url.substring(url.lastIndexOf('/') + 1)
                Files.write(Paths.get(fileName), jsonContent.getBytes())
                println "File downloaded and stored: $fileName"
            } catch (Exception e) {
                println "Error downloading file from $url: ${e.message}"
            }
        }
    }
}

// Example usage:
def initialUrl = "https://example.com/initial.json"
JsonDownloader.downloadJsonFilesWithGreatestVersion(initialUrl)
