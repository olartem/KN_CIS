package com.example.lab5.services;

import com.example.lab5.domain.entities.Coordinate;
import com.example.lab5.domain.entities.Track;
import com.example.lab5.domain.entities.TrackPoint;
import com.example.lab5.dto.TrackDTO;
import com.example.lab5.repositories.TrackPointRepository;
import com.example.lab5.repositories.TrackRepository;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.filter.Filters;
import org.jdom2.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TrackService {
    private final TrackRepository trackRepository;
    private final TrackPointRepository trackPointRepository;

    private static final Logger logger = LoggerFactory.getLogger(TrackService.class);


    public TrackService(TrackRepository trackRepository, TrackPointRepository trackPointRepository) {
        this.trackRepository = trackRepository;
        this.trackPointRepository = trackPointRepository;
    }

    public List<Track> getAllTracks() {
        return trackRepository.findAll();
    }

    public List<TrackDTO> getSimilarTracks(MultipartFile file, int depth) {
        List<TrackDTO> trackDTOList = new ArrayList<>();
        List<Track> existingTracks = trackRepository.findAll();
        Track newTrack = processFile(file);
        for (Track track : existingTracks) {
            double similarity = track.calculateSimilarity(newTrack, depth);
            trackDTOList.add(new TrackDTO(track.getTrackName(), similarity));
        }
        return trackDTOList;
    }

    public boolean saveTrack(MultipartFile file) {
        Track track = processFile(file);
        if(calculateSimilarity(track, 24) <= 0.7) {
            trackRepository.save(track);
            return true;
        }
        else {
            return false;
        }
    }

    private Track processFile(MultipartFile file) {
        // Validate file type
        if (!isValidFileType(file.getOriginalFilename())) {
            throw new IllegalArgumentException("Invalid file type. Only KML or GPX files are allowed.");
        }

        // Parse KML/GPX file and extract coordinates
        List<Coordinate> coordinates = parseFile(file);
        logger.info("Coordinates: {}", coordinates.size());

        Track track = new Track();
        String trackName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9]", "_");
        track.setTrackName(trackName);

        List<TrackPoint> trackPoints = coordinates.stream()
                .map(coordinate -> {
                    TrackPoint trackPoint = new TrackPoint();
                    trackPoint.setSequence(latLongToStr(coordinate.getLatitude(), coordinate.getLongitude(), 24));
                    trackPoint.setTrack(track);
                    return trackPoint;
                })
                .collect(Collectors.toList());

        track.setTrackPoints(trackPoints);
        return track;
    }

    private List<Coordinate> parseFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(inputStream);
            logger.info("Parse");
            // Extract coordinates from the document
            return extractCoordinates(document);
        } catch (IOException | org.jdom2.JDOMException e) {
            throw new RuntimeException("Error parsing KML/GPX file.", e);
        }
    }

    private List<Coordinate> extractCoordinates(Document document) {
        if (document == null) {
            throw new IllegalArgumentException("Document is null.");
        }

        String rootElementName = document.getRootElement().getName();

        if ("gpx".equalsIgnoreCase(rootElementName)) {
            logger.info("Extract gpx");
            return extractGPXCoordinates(document);
        } else if ("kml".equalsIgnoreCase(rootElementName)) {
            logger.info("Extract kml");
            return extractKMLCoordinates(document);
        } else {
            throw new IllegalArgumentException("Unsupported file format. Only GPX and KML files are allowed.");
        }
    }

    private List<Coordinate> extractGPXCoordinates(Document document) {
        Set<Coordinate> coordinates = new HashSet<>();
        XPathFactory xPathFactory = XPathFactory.instance();
        Namespace gpxNamespace = Namespace.getNamespace("g", "http://www.topografix.com/GPX/1/1");

        // Use the namespace in the XPath expression
        XPathExpression<Element> trkptExpression = xPathFactory.compile("//g:trkpt", org.jdom2.filter.Filters.element(), null, gpxNamespace);

        List<Element> trkptElements = trkptExpression.evaluate(document);

        for (Element trkptElement : trkptElements) {
            double latitude = Double.parseDouble(trkptElement.getAttributeValue("lat"));
            double longitude = Double.parseDouble(trkptElement.getAttributeValue("lon"));
            coordinates.add(new Coordinate(latitude, longitude));
        }

        return new ArrayList<>(coordinates);
    }

    private List<Coordinate> extractKMLCoordinates(Document document) {
        Set<Coordinate> coordinates = new HashSet<>();

        XPathFactory xPathFactory = XPathFactory.instance();
        Namespace kmlNamespace = Namespace.getNamespace("kml", "http://earth.google.com/kml/2.1");

        // Use the namespace in the XPath expression
        XPathExpression<Element> coordinatesExpression = xPathFactory.compile("//kml:coordinates[last()]", org.jdom2.filter.Filters.element(), null, kmlNamespace);

        List<Element> coordinatesElements = coordinatesExpression.evaluate(document);

        if (!coordinatesElements.isEmpty()) {
            Element lastCoordinatesElement = coordinatesElements.get(coordinatesElements.size() - 1);
            String[] values = lastCoordinatesElement.getText().trim().split("\\s+");
            for (String value : values) {
                String[] parts = value.split(",");
                if (parts.length == 3) {
                    double longitude = Double.parseDouble(parts[0]);
                    double latitude = Double.parseDouble(parts[1]);
                    coordinates.add(new Coordinate(latitude, longitude));
                }
            }
        }
        logger.info("In extract kml");
        return new ArrayList<>(coordinates);
    }


    private boolean isValidFileType(String fileName) {
        return fileName.toLowerCase().endsWith(".kml") || fileName.toLowerCase().endsWith(".gpx");
    }

    private double calculateSimilarity(Track newTrack, int depth) {
        List<TrackPoint> exisitingTrackPoints = trackPointRepository.findAll();
        List<TrackPoint> newTrackPoins = newTrack.getTrackPoints();

        List<String> existingSequences = new ArrayList<>();
        List<String> newSequences = new ArrayList<>();

        for (TrackPoint point : exisitingTrackPoints) {
            existingSequences.add(point.getSequence().substring(0, depth));
        }

        for (TrackPoint point : newTrackPoins) {
            newSequences.add(point.getSequence().substring(0, depth));
        }
        existingSequences.retainAll(newSequences);

        return (double) existingSequences.size() / newSequences.size();
    }

    private String latLongToStr(double latitude, double longitude, int depth) {
        //logger.info("In extract latLongToStr");
        double minLat = -90.0;
        double maxLat = 90.0;
        double minLon = -180.0;
        double maxLon = 180.0;

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            double latMid = (minLat + maxLat) / 2;
            double lonMid = (minLon + maxLon) / 2;

            if (longitude < lonMid && latitude > latMid) {
                result.append("0");
                maxLon = lonMid;
                minLat = latMid;
            }

            if (longitude > lonMid && latitude > latMid) {
                result.append("1");
                minLon = lonMid;
                minLat = latMid;
            }

            if (longitude < lonMid && latitude < latMid) {
                result.append("2");
                maxLon = lonMid;
                maxLat = latMid;
            }

            if (longitude > lonMid && latitude < latMid) {
                result.append("3");
                minLon = lonMid;
                maxLat = latMid;
            }
        }

        return result.toString();
    }
}

