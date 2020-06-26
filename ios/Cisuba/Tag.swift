//
//  Tag.swift
//  Cisuba
//
//  Created by Sung Kyungmo on 2017. 5. 1..
//  Copyright © 2017년 EastBlue. All rights reserved.
//

import Gloss

struct Tag: Decodable {
    
    let name:String?
    let code:Int?
    let image:String?
    
    init?(json: JSON) {
        self.name = "name" <~~ json
        self.code = "code" <~~ json
        self.image = "image" <~~ json
    }
}
