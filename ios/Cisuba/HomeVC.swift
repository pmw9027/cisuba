//
//  HomeVC.swift
//  Cisuba
//
//  Created by Sung Kyungmo on 2017. 4. 28..
//  Copyright © 2017년 EastBlue. All rights reserved.
//

import UIKit
import ESTabBarController_swift
import ImageSlideshow
import Alamofire
import DropDown
import Gloss

class HomeVC: UIViewController, UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout{
    
    @IBOutlet weak var TopProductCollectionView: UICollectionView!
    @IBOutlet var bannerImageSlider: ImageSlideshow!
    @IBOutlet weak var dropDownCityBtn: UIButton!
    @IBOutlet weak var scrollView: UIScrollView!
    
    let sectionInsets = UIEdgeInsets(top: 50.0, left: 20.0, bottom: 50.0, right: 20.0)
    let itemsPerRow: CGFloat = 3
    
    let localSource = [ImageSource(imageString: "banner_1")!, ImageSource(imageString: "banner_2")!]
    
    var dropDownCity = DropDown()
    var topProduct = [Product]()
    let cisubaURL = "http://cisuba.net"
    
    override func viewDidLoad() {
        getTopProduct("1")
        super.viewDidLoad()
        setBannerImage(bannerImageSlider)
        initDropDownCity()
        
        TopProductCollectionView.dataSource = self
        TopProductCollectionView.delegate = self
        
    }
    func getTopProduct(_ area:String){
        Alamofire.request("http://cisuba.net/get_topareapartner_list",method: .get, parameters: ["area": area,"size":"4"]).responseJSON { response in
            
            if let json = response.result.value {
                self.topProduct = [Product].from(jsonArray: json as! [JSON])!
                
                self.TopProductCollectionView.reloadData()
            }
        }
    }
    
    @IBAction func dropDownCityTap(_ sender: Any) {
        dropDownCity.show()
    }
    
    func initDropDownCity() {
        dropDownCity.anchorView = dropDownCityBtn
        dropDownCity.dataSource = ["Car", "Motorcycle", "Truck"]
        dropDownCity.selectionAction = { [unowned self] (index: Int, item: String) in
            self.dropDownCityBtn.setTitle(item, for: .normal)
        }
    }
    func setBannerImage(_ slider:ImageSlideshow) {
        slider.setImageInputs(localSource)
        slider.slideshowInterval = 5
        slider.contentScaleMode = .scaleAspectFill
        let gestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(bannerTap))
        slider.addGestureRecognizer(gestureRecognizer)
    }
    
    func bannerTap() {
        bannerImageSlider.presentFullScreenController(from: self)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView,
                        numberOfItemsInSection section: Int) -> Int {
        return self.topProduct.count
    }
    
    
    func collectionView(_ collectionView: UICollectionView,
                        cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = TopProductCollectionView.dequeueReusableCell(withReuseIdentifier: "topProductCell", for: indexPath) as! TopProductCollectionViewCell
        
        let product = self.topProduct[indexPath.row]
        
        cell.name.text = product.partnerName
        if let url = URL(string: cisubaURL + product.mainThumbnail!){
            cell.backgroundImage.af_setImage(withURL: url)
        }
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        print("did select:      \(indexPath.row)  ")
    }
    
}

