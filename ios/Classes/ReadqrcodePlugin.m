#import "ReadqrcodePlugin.h"
#if __has_include(<readqrcode/readqrcode-Swift.h>)
#import <readqrcode/readqrcode-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "readqrcode-Swift.h"
#endif

@implementation ReadqrcodePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftReadqrcodePlugin registerWithRegistrar:registrar];
}
@end
