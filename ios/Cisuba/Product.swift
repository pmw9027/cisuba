//
//  Product.swift
//  Cisuba
//
//  Created by Sung Kyungmo on 2017. 5. 1..
//  Copyright © 2017년 EastBlue. All rights reserved.
//

import Gloss

struct Product: Decodable {
    
    let id: Int?
    let partnerName: String?
    let mainThumbnail: String?
    let hit: Int?
    let isFreePartner: Bool?
    let gubunAdress: String?
    let highlightAddress: String?
    let shortAddress: String?
    let detailAddress: String?
    let detailAbout: String?
    let useAbout: String?
    let startStime: String?
    let startEtime: String?
    let phone: String?
    let discount: Int?
    let lat: Double?
    let lng: Double?
    let morningSTime: String?
    let morningETime: String?
    let lunchSTime: String?
    let lunchETime: String?
    let dinnerSTime: String?
    let dinnerETime: String?
    let morningPrice: Int?
    let lunchPrice: Int?
    let dinnerPrice: Int?
    let tag: [Tag]?
    let partnerproduct_image: [Partnerproduct_image]?
    // MARK: - Deserialization
    
    
    init?(json: JSON) {
        self.id = "id" <~~ json
        self.partnerName = "partnerName" <~~ json
        self.mainThumbnail = "mainThumbnail" <~~ json
        self.hit = "hit" <~~ json
        self.isFreePartner = "isFreePartner" <~~ json
        self.gubunAdress = "gubunAdress" <~~ json
        self.highlightAddress = "highlightAddress" <~~ json
        self.shortAddress = "shortAddress" <~~ json
        self.detailAddress = "detailAddress" <~~ json
        self.detailAbout = "detailAbout" <~~ json
        self.useAbout = "useAbout" <~~ json
        self.startStime = "startStime" <~~ json
        self.startEtime = "startEtime" <~~ json
        self.phone = "phone" <~~ json
        self.discount = "discount" <~~ json
        self.lat = "lat" <~~ json
        self.lng = "lng" <~~ json
        self.morningSTime = "morningSTime" <~~ json
        self.morningETime = "morningETime" <~~ json
        self.lunchSTime = "lunchSTime" <~~ json
        self.lunchETime = "lunchETime" <~~ json
        self.dinnerSTime = "dinnerSTime" <~~ json
        self.dinnerETime = "dinnerETime" <~~ json
        self.morningPrice = "morningPrice" <~~ json
        self.lunchPrice = "lunchPrice" <~~ json
        self.dinnerPrice = "dinnerPrice" <~~ json
        self.tag = "tag" <~~ json
        self.partnerproduct_image = "partnerproduct_image" <~~ json
    }
    
}
struct Partnerproduct_image: Decodable  {
    let image:String?
    init?(json: JSON) {
        self.image = "image" <~~ json
    }
}
